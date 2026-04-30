package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.data.mapper.toDomain
import com.matildaerenius.bookmap.domain.model.FavoriteBook
import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<FavoriteBook>> {
        return favoriteDao.getFavoritesWithDetails().map { relations ->
            relations.map { it.toDomain() }
        }
    }

    override suspend fun addFavorite(bookId: Int, savedAt: Long) {
        val entity = FavoriteEntity(bookId = bookId, savedAt = savedAt)
        favoriteDao.insertFavorite(entity)
    }

    override suspend fun removeFavorite(bookId: Int) {
        favoriteDao.deleteFavorite(bookId)
    }
}