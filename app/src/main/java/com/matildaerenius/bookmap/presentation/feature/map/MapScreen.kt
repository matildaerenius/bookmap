package com.matildaerenius.bookmap.presentation.feature.map

import BookSummarySheet
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.components.MapMarkerIcon
import com.matildaerenius.bookmap.presentation.common.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedMarker by viewModel.selectedMarker.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConstants.STOCKHOLM_CENTER, 12f)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

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
            val message = (uiState as UiState.Error).message
            Log.e("BookMap", "MapScreen: Error message: $message")
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
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
                            Log.i("BookMap", "MapScreen: User clicked on marker for bokID: ${bookMarker.bookId}")
                            true
                        }
                    ) {
                        MapMarkerIcon(
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }
            }
        }

        if (uiState is UiState.Loading) {
            FullScreenLoadingIndicator()
        }

        if (selectedMarker != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(MapEvent.OnDismissBottomSheet) },
                sheetState = sheetState,
                containerColor = Color.Black.copy(alpha = 0.9f),
                scrimColor = Color.Transparent,
                dragHandle = { },
                modifier = Modifier.fillMaxHeight()

            ) {
                BookSummarySheet(
                    marker = selectedMarker!!,
                    onCardClick = {
                        viewModel.onEvent(MapEvent.OnDismissBottomSheet)
                        onNavigateToDetail(selectedMarker!!.bookId)
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