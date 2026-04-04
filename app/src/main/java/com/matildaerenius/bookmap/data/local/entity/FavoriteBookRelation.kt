package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Embedded

data class FavoriteBookRelation(
    @Embedded val favorite: FavoriteEntity,
    @Embedded val marker: MarkerEntity
)