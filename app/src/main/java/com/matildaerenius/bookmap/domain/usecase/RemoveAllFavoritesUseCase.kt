package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveAllFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke() {
        repository.removeAllFavorites()
    }
}