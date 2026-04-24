package com.matildaerenius.bookmap.presentation.feature.map

import com.matildaerenius.bookmap.presentation.feature.map.components.BookSummarySheet
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.presentation.feature.map.components.BookGoogleMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedMarker by viewModel.selectedMarker.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConstants.STOCKHOLM_CENTER, 12f)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    LaunchedEffect(selectedMarker) {
        selectedMarker?.let { marker ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                LatLng(marker.latitude, marker.longitude),
                16f
            )

            cameraPositionState.animate(
                update = cameraUpdate,
                durationMs = 1000
            )
        }
    }

    LaunchedEffect(Unit) {
        Log.i("BookMap", "MapScreen: Force start initial fetch")
        viewModel.onEvent(MapEvent.OnMapBoundsChanged(MapConstants.STOCKHOLM_BOUNDS.toMapBoundingBox()))
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
            val errorMessage = (uiState as UiState.Error).message.asString(context)
            Log.e("BookMap", "MapScreen: Error message: $errorMessage")
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val currentMarkers = if (uiState is UiState.Success) {
            (uiState as UiState.Success).data
        } else {
            emptyList()
        }

        BookGoogleMap(
            markers = currentMarkers,
            cameraPositionState = cameraPositionState,
            onMarkerClick = { bookId ->
                viewModel.onEvent(MapEvent.OnMarkerClick(bookId))
            }
        )

        if (uiState is UiState.Loading) {
            FullScreenLoadingIndicator()
        }

        if (selectedMarker != null) {
            val isFav = favorites.any { it.bookId == selectedMarker!!.bookId }
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(MapEvent.OnDismissBottomSheet) },
                sheetState = sheetState,
                containerColor = Color.Black.copy(alpha = 0.8f),
                scrimColor = Color.Transparent,
                dragHandle = { },
                modifier = Modifier.fillMaxHeight()
            ) {
                BookSummarySheet(
                    marker = selectedMarker!!,
                    isFavorite = isFav,
                    onToggleFavorite = {
                        viewModel.onEvent(MapEvent.OnToggleFavorite(selectedMarker!!.bookId, isFav))
                    }
                )
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

object MapConstants {
    val STOCKHOLM_CENTER = LatLng(59.3293, 18.0686)
    val STOCKHOLM_BOUNDS = LatLngBounds(
        LatLng(59.2700, 17.9000),
        LatLng(59.4000, 18.2500)
    )
}