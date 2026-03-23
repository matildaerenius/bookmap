package com.matildaerenius.bookmap.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val bookId : Int,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
)
