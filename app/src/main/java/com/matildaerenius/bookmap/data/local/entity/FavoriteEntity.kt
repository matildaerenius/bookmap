package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    @PrimaryKey val bookId: Int,
    val savedAt: Long
)