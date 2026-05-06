package com.matildaerenius.bookmap.presentation.feature.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.matildaerenius.bookmap.domain.model.MapBoundingBox

fun LatLngBounds.toMapBoundingBox() = MapBoundingBox(
    southWestLat = this.southwest.latitude,
    southWestLng = this.southwest.longitude,
    northEastLat = this.northeast.latitude,
    northEastLng = this.northeast.longitude
)

object MapConstants {
    val STOCKHOLM_CENTER = LatLng(59.3293, 18.0686)
    val STOCKHOLM_BOUNDS = LatLngBounds(
        LatLng(59.2700, 17.9000),
        LatLng(59.4000, 18.2500)
    )
    val INNER_CITY_BOUNDS = LatLngBounds(
        LatLng(59.3080, 18.0000),
        LatLng(59.3520, 18.1050)
    )
}