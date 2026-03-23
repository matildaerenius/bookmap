package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.util.Resource
import javax.inject.Inject

class GetBookMarkersUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(): Resource<List<BookMapMarker>> {
        val locationsResult = locationRepository.getLocations()

        if (locationsResult is Resource.Error) {
            return Resource.Error(locationsResult.message ?: "Could not load position data")
        }

        val locations = (locationsResult as? Resource.Success)?.data ?: emptyList()

        if (locations.isEmpty()) {
            return Resource.Success(emptyList())
        }

        val bookIds = locations.map { it.bookId }.distinct()

        val booksResult = bookRepository.getBooksByIds(bookIds)

        if (booksResult is Resource.Error) {
            return Resource.Error(booksResult.message ?: "Could not load books")
        }

        val books = (booksResult as? Resource.Success)?.data ?: emptyList()

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

        return Resource.Success(markers)
    }
}