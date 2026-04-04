package com.matildaerenius.bookmap.domain.model

data class FavoriteBook(
    val bookId: Int,
    val marker: BookMapMarker?
)