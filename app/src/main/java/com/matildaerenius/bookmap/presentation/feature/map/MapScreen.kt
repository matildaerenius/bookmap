package com.matildaerenius.bookmap.presentation.feature.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.presentation.common.components.MapMarkerIcon
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.state.UiState
import androidx.compose.ui.platform.LocalContext
import com.matildaerenius.bookmap.R
import com.google.android.gms.maps.model.MapStyleOptions

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val stockholmCenter = LatLng(59.3293, 18.0686)
    val stockholmBounds = LatLngBounds(
        LatLng(59.2700, 17.9000),
        LatLng(59.4000, 18.2500)
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(stockholmCenter, 12f)
    }

    LaunchedEffect(Unit) {
        Log.i("BookMap", "MapScreen: Force start initial fetch")
        viewModel.onEvent(MapEvent.OnMapBoundsChanged(stockholmBounds.toMapBoundingBox()))
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.let { bounds ->
                Log.d("BookMap", "MapScreen: Camera stopped at $bounds")
                viewModel.onEvent(MapEvent.OnMapBoundsChanged(bounds.toMapBoundingBox()))
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            val message = (uiState as UiState.Error).message
            Log.e("BookMap", "MapScreen: Error message: $message")
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                minZoomPreference = 10f,
                maxZoomPreference = 18f,
                latLngBoundsForCameraTarget = stockholmBounds,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = false
            )
        ) {
            if (uiState is UiState.Success) {
                val markers = (uiState as UiState.Success).data
                markers.forEach { bookMarker ->
                    MarkerComposable(
                        keys = arrayOf(bookMarker.bookId),
                        state = MarkerState(
                            position = LatLng(bookMarker.latitude, bookMarker.longitude)
                        ),
                        onClick = {
                            viewModel.onEvent(MapEvent.OnMarkerClick(bookMarker.bookId))
                            onNavigateToDetail(bookMarker.bookId)
                            Log.i("BookMap", "MapScreen: User clicked on marker for bokID: ${bookMarker.bookId}")
                            true
                        }
                    ) {
                        MapMarkerIcon(
                            modifier = Modifier
                                .size(30.dp),
                        )
                    }
                }
            }
        }

        when (uiState) {
            is UiState.Loading -> {
                FullScreenLoadingIndicator()
            }
            is UiState.Success, is UiState.Error -> {
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}
fun LatLngBounds.toMapBoundingBox() = MapBoundingBox(
    southWestLat = this.southwest.latitude,
    southWestLng = this.southwest.longitude,
    northEastLat = this.northeast.latitude,
    northEastLng = this.northeast.longitude
)