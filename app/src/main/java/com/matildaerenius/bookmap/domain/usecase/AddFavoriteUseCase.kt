package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(bookId: Int) {
        favoriteRepository.addFavorite(
            bookId = bookId,
            savedAt = System.currentTimeMillis()
        )
    }
}