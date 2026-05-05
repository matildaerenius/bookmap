package com.matildaerenius.bookmap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.dao.MarkerDao
import com.matildaerenius.bookmap.data.local.dao.VisitedDao
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity
import com.matildaerenius.bookmap.data.local.entity.VisitedEntity

@Database(
    entities = [MarkerEntity::class, FavoriteEntity::class, VisitedEntity::class],
    version = 5,
    exportSchema = true
)
abstract class BookMapDatabase : RoomDatabase() {

    abstract val markerDao: MarkerDao
    abstract val favoriteDao: FavoriteDao
    abstract val visitedDao: VisitedDao

}