package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveBookMarkersUseCase @Inject constructor(
    private val markerRepository: MarkerRepository
) {
    operator fun invoke(currentBoundsFlow: Flow<MapBoundingBox?>): Flow<List<BookMapMarker>> {
        return combine(
            markerRepository.observeMarkers(),
            currentBoundsFlow
        ) { allMarkers, bounds ->
            if (bounds == null) return@combine emptyList<BookMapMarker>()

            allMarkers.filter { marker ->
                marker.latitude in bounds.southWestLat..bounds.northEastLat &&
                        marker.longitude in bounds.southWestLng..bounds.northEastLng
            }
        }
    }
}