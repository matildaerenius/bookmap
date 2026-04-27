package com.matildaerenius.bookmap.presentation.feature.map.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.matildaerenius.bookmap.domain.model.FavoriteBook
import com.matildaerenius.bookmap.presentation.common.components.MapMarkerIcon
import com.matildaerenius.bookmap.presentation.feature.map.MapConstants

@Composable
fun BookGoogleMap(
    markers: List<BookMapMarker>,
    favorites: List<FavoriteBook>,
    cameraPositionState: CameraPositionState,
    onMarkerClick: (Int) -> Unit,
    onMapLoaded: () -> Unit
) {
    val context = LocalContext.current

    val favoriteIds = remember(favorites) {
        favorites.map { it.bookId }.toSet()
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = onMapLoaded,
        properties = MapProperties(
            minZoomPreference = 10f,
            maxZoomPreference = 18f,
            latLngBoundsForCameraTarget = MapConstants.STOCKHOLM_BOUNDS,
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false
        )
    ) {
        markers.forEach { bookMarker ->
            val isFavorite = favoriteIds.contains(bookMarker.bookId)
            MarkerComposable(
                keys = arrayOf(bookMarker.bookId, isFavorite),
                state = rememberUpdatedMarkerState(
                    position = LatLng(bookMarker.latitude, bookMarker.longitude)
                ),
                onClick = {
                    onMarkerClick(bookMarker.bookId)
                    true
                }
            ) {
                MapMarkerIcon(
                    isFavorite = isFavorite,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}