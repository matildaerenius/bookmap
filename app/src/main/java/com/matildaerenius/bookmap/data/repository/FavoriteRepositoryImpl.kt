package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.entity.FavoriteBookRelation
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<FavoriteBookRelation>> {
        return favoriteDao.getFavoritesWithDetails()
    }

    override suspend fun addFavorite(favorite: FavoriteEntity) {
        favoriteDao.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(bookId: String) {
        favoriteDao.deleteFavorite(bookId)
    }
}