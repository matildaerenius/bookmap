package com.matildaerenius.bookmap.domain.model

data class BookMapMarker(
    val bookId: Int,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val bookTitle: String,
    val bookAuthor: String,
    val bookImageUrl: String,
    val isVisited: Boolean
)
