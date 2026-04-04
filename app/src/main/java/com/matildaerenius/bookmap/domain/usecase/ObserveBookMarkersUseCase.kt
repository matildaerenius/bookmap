package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBookMarkersUseCase @Inject constructor(
    private val markerRepository: MarkerRepository
) {
    operator fun invoke(): Flow<List<BookMapMarker>> {
        return markerRepository.observeMarkers()
    }
}