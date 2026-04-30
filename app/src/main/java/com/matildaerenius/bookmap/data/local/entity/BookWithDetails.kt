package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookWithDetails(
    @Embedded val marker: MarkerEntity,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val visited: VisitedEntity?,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val favorite: FavoriteEntity?
)