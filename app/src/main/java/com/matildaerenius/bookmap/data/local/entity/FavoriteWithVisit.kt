package com.matildaerenius.bookmap.data.local.entity

import androidx.room.Embedded

data class FavoriteWithVisit(
    @Embedded val favorite: FavoriteEntity,
    val isVisited: Boolean
)
