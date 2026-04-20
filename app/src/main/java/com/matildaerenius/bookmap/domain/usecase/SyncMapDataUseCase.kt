package com.matildaerenius.bookmap.domain.usecase

import android.util.Log
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
    suspend operator fun invoke(boundingBox: MapBoundingBox): Resource<List<BookMapMarker>>{
        Log.d("BookMap", "USECASE 1: Startar för boundingBox $boundingBox")
        Log.d("BookMap", "USECASE 2: Anropar LocationRepository (Gist)...")
        val locationsResult = locationRepository.getLocations()
        Log.d("BookMap", "USECASE 3: Fick svar från Gist!")

        if (locationsResult is Resource.Error) {
            Log.d("BookMap", "USECASE 4: Gist returnerade Error: ${locationsResult.error}")
            return Resource.Error(locationsResult.error)
        }

        val allLocations = (locationsResult as Resource.Success).data
        Log.d("BookMap", "USECASE 5: Hittade ${allLocations.size} platser totalt.")

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
                    bookImageUrl = book.imageUrl
                )
            } else {
                null
            }
        }

        markerRepository.upsertMarkers(markers)
        return Resource.Success(markers)
    }
}