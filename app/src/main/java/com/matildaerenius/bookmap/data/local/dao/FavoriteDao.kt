package com.matildaerenius.bookmap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.data.local.entity.FavoriteWithVisit
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_entity WHERE bookId = :bookId")
    suspend fun deleteFavorite(bookId: Int)

    @Query(
        """
        SELECT 
            f.*, 
            (v.bookId IS NOT NULL) AS isVisited
        FROM favorite_entity f
        LEFT JOIN visited_entity v ON f.bookId = v.bookId
        ORDER BY f.savedAt DESC
    """
    )
    fun getFavoritesWithDetails(): Flow<List<FavoriteWithVisit>>

    @Query("SELECT * FROM favorite_entity")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>
}