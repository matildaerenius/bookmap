package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FavoriteBookRelation(
    @Embedded val favorite: FavoriteEntity,

    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val marker: MarkerEntity?,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    val visited: VisitedEntity?
)