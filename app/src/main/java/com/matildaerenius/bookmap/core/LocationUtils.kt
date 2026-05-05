package com.matildaerenius.bookmap.core

import android.location.Location
import kotlin.math.roundToInt

fun getFormattedDistance(
    userLat: Double,
    userLng: Double,
    markerLat: Double,
    markerLng: Double
): String {
    val results = FloatArray(1)
    Location.distanceBetween(userLat, userLng, markerLat, markerLng, results)

    val distanceInMeters = results[0]

    return if (distanceInMeters < 1000) {
        "${distanceInMeters.roundToInt()} m"
    } else {
        val distanceInKm = distanceInMeters / 1000.0
        String.format(java.util.Locale.getDefault(), "%.1f km", distanceInKm)
    }
}