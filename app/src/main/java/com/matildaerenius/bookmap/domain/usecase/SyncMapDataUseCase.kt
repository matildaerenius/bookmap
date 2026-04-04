package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import com.matildaerenius.bookmap.util.Resource
import javax.inject.Inject

class SyncMapDataUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val bookRepository: BookRepository,
    private val markerRepository: MarkerRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        val locationsResult = locationRepository.getLocations()

        if (locationsResult is Resource.Error) {
            return Resource.Error(locationsResult.error)
        }

        val locations = (locationsResult as Resource.Success).data

        if (locations.isEmpty()) {
            return Resource.Success(Unit)
        }

        val bookIds = locations.map { it.bookId }.distinct()
        val booksResult = bookRepository.getBooksByIds(bookIds)

        if (booksResult is Resource.Error) {
            return Resource.Error(booksResult.error)
        }

        val books = (booksResult as Resource.Success).data
        val bookMap = books.associateBy { it.id }

        val markers = locations.mapNotNull { location ->
            val book = bookMap[location.bookId]

            if (book != null) {
                BookMapMarker(
                    bookId = location.bookId,
                    locationName = location.locationName,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    description = location.description,
                    bookTitle = book.title,
                    bookAuthor = book.author,
                    bookImageUrl = book.imageUrl
                )
            } else {
                null
            }
        }

        markerRepository.replaceCache(markers)
        return Resource.Success(Unit)
    }
}