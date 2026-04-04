package com.matildaerenius.bookmap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matildaerenius.bookmap.data.local.entity.FavoriteBookRelation
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_entity WHERE bookId = :bookId")
    suspend fun deleteFavorite(bookId: String)

    @Query("""
        SELECT * FROM favorite_entity 
        INNER JOIN marker_entity ON favorite_entity.bookId = marker_entity.bookId
        ORDER BY favorite_entity.savedAt DESC
    """)
    fun getFavoritesWithDetails(): Flow<List<FavoriteBookRelation>>
}