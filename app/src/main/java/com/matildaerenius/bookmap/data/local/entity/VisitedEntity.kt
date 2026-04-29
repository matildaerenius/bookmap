package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visited_entity")
data class VisitedEntity(
    @PrimaryKey val bookId: Int,
    val visitedAt: Long = System.currentTimeMillis()
)