package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marker_entity")
data class MarkerEntity(
    @PrimaryKey val bookId: Int,
    val title: String,
    val author: String,
    val description: String,
    val coverImageUrl: String,
    val ebook: Boolean,
    val audio: Boolean,
    val locationDescription: String,
    val latitude: Double,
    val longitude: Double
)