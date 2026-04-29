package com.matildaerenius.bookmap.presentation.feature.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.presentation.common.components.MapMarkerIcon
import com.matildaerenius.bookmap.presentation.feature.map.MapConstants

@Composable
fun BookGoogleMap(
    hasLocationPermission: Boolean,
    markers: List<BookMapMarker>,
    cameraPositionState: CameraPositionState,
    onMarkerClick: (Int) -> Unit,
    onMapLoaded: () -> Unit
) {
    val context = LocalContext.current

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = onMapLoaded,
        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission,
            minZoomPreference = 12f,
            maxZoomPreference = 18f,
            latLngBoundsForCameraTarget = MapConstants.INNER_CITY_BOUNDS,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false,
        )
    ) {
        markers.forEach { bookMarker ->
            MarkerComposable(
                keys = arrayOf(bookMarker.bookId, bookMarker.isFavorite, bookMarker.isVisited),
                state = rememberUpdatedMarkerState(
                    position = LatLng(bookMarker.latitude, bookMarker.longitude)
                ),
                onClick = {
                    onMarkerClick(bookMarker.bookId)
                    true
                }
            ) {
                MapMarkerIcon(
                    isFavorite = bookMarker.isFavorite,
                    isVisited = bookMarker.isVisited,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}