package com.matildaerenius.bookmap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity

@Dao
interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkers(markers: List<MarkerEntity>)

    @Query("SELECT * FROM marker_entity")
    suspend fun getAllMarkers(): List<MarkerEntity>

    @Query("DELETE FROM marker_entity")
    suspend fun clearMarkers()
}