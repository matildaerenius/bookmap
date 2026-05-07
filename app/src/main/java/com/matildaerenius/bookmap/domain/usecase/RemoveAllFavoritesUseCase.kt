package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.repository.BookRepository
import javax.inject.Inject

class RemoveAllFavoritesUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke() {
        repository.removeAllFavorites()
    }
}