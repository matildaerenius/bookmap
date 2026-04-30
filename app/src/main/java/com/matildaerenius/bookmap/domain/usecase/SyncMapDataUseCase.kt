package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import com.matildaerenius.bookmap.core.Resource
import javax.inject.Inject

class SyncMapDataUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val bookRepository: BookRepository,
    private val markerRepository: MarkerRepository
) {
    suspend operator fun invoke(boundingBox: MapBoundingBox): Resource<List<BookMapMarker>>{
        val locationsResult = locationRepository.getLocations()

        if (locationsResult is Resource.Error) {
            return Resource.Error(locationsResult.error)
        }

        val allLocations = (locationsResult as Resource.Success).data

        val visibleLocations = allLocations.filter { location ->
            val inLatRange = location.latitude >= boundingBox.southWestLat &&
                    location.latitude <= boundingBox.northEastLat

            val inLngRange = if (boundingBox.northEastLng >= boundingBox.southWestLng) {
                location.longitude >= boundingBox.southWestLng &&
                        location.longitude <= boundingBox.northEastLng
            } else {
                location.longitude >= boundingBox.southWestLng ||
                        location.longitude <= boundingBox.northEastLng
            }

            inLatRange && inLngRange
        }

        if (visibleLocations.isEmpty()) {
            return Resource.Success((emptyList()))
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
                    bookImageUrl = book.imageUrl,
                    isVisited = false,
                    isFavorite = false
                )
            } else {
                null
            }
        }

        markerRepository.upsertMarkers(markers)
        return Resource.Success(markers)
    }
}