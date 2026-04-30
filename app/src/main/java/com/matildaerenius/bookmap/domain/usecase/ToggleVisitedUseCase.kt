package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import javax.inject.Inject

class ToggleVisitedUseCase @Inject constructor(
    private val repository: MarkerRepository
) {
    suspend operator fun invoke(bookId: Int, isVisited: Boolean) {
        repository.updateVisitedStatus(bookId, isVisited)
    }
}