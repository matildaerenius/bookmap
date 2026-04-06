package com.matildaerenius.bookmap.domain.model

data class MapBoundingBox(
    val northEastLat: Double,
    val northEastLng: Double,
    val southWestLat: Double,
    val southWestLng: Double
)