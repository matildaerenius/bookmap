package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    @PrimaryKey val bookId: Int,
    val savedAt: Long,
    val title: String,
    val author: String,
    val description: String,
    val locationName: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val ebook: Boolean,
    val audio: Boolean
)