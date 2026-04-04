package com.matildaerenius.bookmap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.dao.MarkerDao
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity

@Database(
    entities = [MarkerEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookMapDatabase : RoomDatabase() {

    abstract val markerDao: MarkerDao
    abstract val favoriteDao: FavoriteDao

}