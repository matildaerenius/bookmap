package com.matildaerenius.bookmap.domain.repository

import com.matildaerenius.bookmap.data.local.entity.FavoriteBookRelation
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<FavoriteBookRelation>>
    suspend fun addFavorite(favorite: FavoriteEntity)
    suspend fun removeFavorite(bookId: String)
}