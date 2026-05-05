package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(marker: BookMapMarker) {
        favoriteRepository.addFavorite(
            marker = marker,
            savedAt = System.currentTimeMillis()
        )
    }
}