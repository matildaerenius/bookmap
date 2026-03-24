package com.matildaerenius.bookmap.domain.model

data class BookLocation(
    val bookId: Int,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)
