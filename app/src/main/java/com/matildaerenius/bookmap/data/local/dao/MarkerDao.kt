package com.matildaerenius.bookmap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.matildaerenius.bookmap.data.local.entity.BookWithDetails
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMarkers(markers: List<MarkerEntity>)

    @Query("SELECT * FROM marker_entity")
    fun getAllMarkers(): Flow<List<MarkerEntity>>

    @Transaction
    @Query("SELECT * FROM marker_entity")
    fun getMarkersWithDetails(): Flow<List<BookWithDetails>>

    @Query("SELECT bookId FROM marker_entity")
    suspend fun getAllMarkerIds(): List<Int>

    @Query("DELETE FROM marker_entity WHERE bookId IN (:ids)")
    suspend fun deleteMarkersByIds(ids: List<Int>)
}