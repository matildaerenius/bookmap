package com.matildaerenius.bookmap.presentation.feature.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.matildaerenius.bookmap.presentation.common.components.BookMarkerIcon
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.state.UiState

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val stockholmCenter = LatLng(59.3293, 18.0686)
    val stockholmBounds = LatLngBounds(
        LatLng(59.2700, 17.9000),
        LatLng(59.4000, 18.2500)
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(stockholmCenter, 12f)
    }

    LaunchedEffect(Unit) {
        Log.d("BookMap", "MapScreen: Force start initial fetch")
        val initialBox = MapBoundingBox(
            southWestLat = stockholmBounds.southwest.latitude,
            southWestLng = stockholmBounds.southwest.longitude,
            northEastLat = stockholmBounds.northeast.latitude,
            northEastLng = stockholmBounds.northeast.longitude
        )
        viewModel.onEvent(MapEvent.OnMapBoundsChanged(initialBox))
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            cameraPositionState.projection?.visibleRegion?.latLngBounds?.let { bounds ->
                Log.d("BookMap", "MapScreen: Camera stopped at $bounds")
                val domainBounds = MapBoundingBox(
                    southWestLat = bounds.southwest.latitude,
                    southWestLng = bounds.southwest.longitude,
                    northEastLat = bounds.northeast.latitude,
                    northEastLng = bounds.northeast.longitude
                )
                viewModel.onEvent(MapEvent.OnMapBoundsChanged(domainBounds))
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            val message = (uiState as UiState.Error).message
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
                latLngBoundsForCameraTarget = stockholmBounds
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
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
                            true
                        }
                    ) {
                        BookMarkerIcon()
                    }
                }
            }
        }

        if (uiState is UiState.Loading) {
            FullScreenLoadingIndicator()
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}