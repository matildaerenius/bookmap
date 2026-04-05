package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
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
    suspend operator fun invoke(boundingBox: MapBoundingBox): Resource<Unit> {
        val locationsResult = locationRepository.getLocations()

        if (locationsResult is Resource.Error) {
            return Resource.Error(locationsResult.error)
        }

        val allLocations = (locationsResult as Resource.Success).data

        val visibleLocations = allLocations.filter { location ->
            location.latitude <= boundingBox.northEastLat &&
                    location.latitude >= boundingBox.southWestLat &&
                    location.longitude <= boundingBox.northEastLng &&
                    location.longitude >= boundingBox.southWestLng
        }

        if (visibleLocations.isEmpty()) {
            return Resource.Success(Unit)
        }

        val bookIds = visibleLocations.map { it.bookId }.distinct()
        val booksResult = bookRepository.getBooksByIds(bookIds)

        if (booksResult is Resource.Error) {
            return Resource.Error(booksResult.error)
        }

        val books = (booksResult as Resource.Success).data
        val bookMap = books.associateBy { it.id }

        val markers = visibleLocations.mapNotNull { location ->
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

        markerRepository.upsertMarkers(markers)
        return Resource.Success(Unit)
    }
}