package com.matildaerenius.bookmap.presentation.feature.map

import com.matildaerenius.bookmap.presentation.feature.map.components.BookSummarySheet
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.presentation.feature.map.components.BookGoogleMap
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.ui.res.stringResource
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.presentation.common.components.FloatingActionButtonItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    hasLocationPermission: Boolean,
    onMapLoaded: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val appContext = context.applicationContext
    val fusedLocationClient = remember(appContext) {
        LocationServices.getFusedLocationProviderClient(appContext)
    }
    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConstants.STOCKHOLM_CENTER, 13f)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(state.selectedMarker) {
        state.selectedMarker?.let { marker ->
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

    LaunchedEffect(cameraPositionState) {
        snapshotFlow {
            if (!cameraPositionState.isMoving) {
                cameraPositionState.projection?.visibleRegion?.latLngBounds
            } else {
                null
            }
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { bounds ->
                Log.d("BookMap", "MapScreen: Camera settled at $bounds")
                viewModel.onEvent(MapEvent.OnMapBoundsChanged(bounds.toMapBoundingBox()))
            }
    }

    LaunchedEffect(state.markersState) {
        if (state.markersState is UiState.Error) {
            val errorMessage = (state.markersState as UiState.Error).message.asString(context)
            Log.e("BookMap", "MapScreen: Error message: $errorMessage")
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val currentMarkers = mutableListOf<BookMapMarker>()
        if (state.markersState is UiState.Success) {
            currentMarkers.addAll((state.markersState as UiState.Success).data)
        }
        state.selectedMarker?.let { marker ->
            if (currentMarkers.none { it.bookId == marker.bookId }) {
                currentMarkers.add(marker)
            }
        }

        BookGoogleMap(
            markers = currentMarkers,
            cameraPositionState = cameraPositionState,
            onMapLoaded = onMapLoaded,
            hasLocationPermission = hasLocationPermission,
            onMarkerClick = { bookId ->
                viewModel.onEvent(MapEvent.OnMarkerClick(bookId))
            },
        )
        if (hasLocationPermission) {
            FloatingActionButtonItem(
                selected = false,
                onClick = {
                    @SuppressLint("MissingPermission")
                    val locationTask = fusedLocationClient.lastLocation

                    locationTask.addOnSuccessListener { location ->
                        if (location != null) {
                            coroutineScope.launch {
                                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                    LatLng(location.latitude, location.longitude),
                                    15f
                                )
                                cameraPositionState.animate(
                                    update = cameraUpdate,
                                    durationMs = 1000
                                )
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = stringResource(id = R.string.go_to_my_location),
                        tint = Color.Black
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 192.dp, end = 16.dp)
            )
        }

        if (state.markersState is UiState.Loading) {
            FullScreenLoadingIndicator()
        }

        if (state.selectedMarker != null) {
            val freshMarker = currentMarkers.find { it.bookId == state.selectedMarker!!.bookId } ?: state.selectedMarker!!
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(MapEvent.OnDismissBottomSheet) },
                sheetState = sheetState,
                containerColor = Color.Transparent,
                scrimColor = Color.Transparent,
                contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
                tonalElevation = 0.dp,
                dragHandle = { },
                modifier = Modifier.fillMaxHeight()
            ) {
                BookSummarySheet(
                    marker = freshMarker,
                    onClose = {
                        coroutineScope.launch {
                            sheetState.hide()
                            viewModel.onEvent(MapEvent.OnDismissBottomSheet)
                        }
                    },
                    onToggleFavorite = {
                        viewModel.onEvent(
                            MapEvent.OnToggleFavorite(
                                state.selectedMarker!!.bookId,
                                freshMarker.isFavorite
                            )
                        )
                    },
                    onAddClick = {
                        viewModel.onEvent(
                            MapEvent.OnToggleVisited(
                                state.selectedMarker!!.bookId,
                                freshMarker.isVisited
                            )
                        )
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
    val INNER_CITY_BOUNDS = LatLngBounds(
        LatLng(59.3080, 18.0000),
        LatLng(59.3520, 18.1050)
    )

}