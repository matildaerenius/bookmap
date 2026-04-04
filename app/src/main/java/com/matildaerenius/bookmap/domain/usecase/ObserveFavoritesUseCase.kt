package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.data.local.entity.FavoriteBookRelation
import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<FavoriteBookRelation>> {
        return favoriteRepository.getFavorites()
    }
}