package com.matildaerenius.bookmap.domain.repository

import com.matildaerenius.bookmap.domain.model.FavoriteBook
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<FavoriteBook>>
    suspend fun addFavorite(bookId: Int, savedAt: Long)
    suspend fun removeFavorite(bookId: Int)
}