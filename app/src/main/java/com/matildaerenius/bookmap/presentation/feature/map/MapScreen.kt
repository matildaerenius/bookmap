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
import com.google.maps.android.compose.*
import com.matildaerenius.bookmap.presentation.common.components.FullScreenLoadingIndicator
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.presentation.feature.map.components.BookGoogleMap
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.WindowInsets
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.matildaerenius.bookmap.core.getFormattedDistance
import com.matildaerenius.bookmap.presentation.feature.map.components.MapActionButtons
import com.matildaerenius.bookmap.presentation.feature.map.components.MapFilterDialog
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
        getFusedLocationProviderClient(appContext)
    }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(MapConstants.STOCKHOLM_CENTER, 13f)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val currentMarkers = remember(state.markersState, state.mapFilter, state.selectedMarker) {
        when (val markersState = state.markersState) {
            is UiState.Success -> {
                val allVisibleMarkers = markersState.data

                val filtered = when (state.mapFilter) {
                    MapFilter.ALL -> allVisibleMarkers
                    MapFilter.FAVORITES_ONLY -> allVisibleMarkers.filter { it.isFavorite }
                    MapFilter.VISITED_ONLY -> allVisibleMarkers.filter { it.isVisited }
                    MapFilter.UNVISITED_ONLY -> allVisibleMarkers.filter { !it.isVisited }
                }

                val selected = state.selectedMarker
                if (selected != null && filtered.none { it.bookId == selected.bookId }) {
                    filtered + selected
                } else {
                    filtered
                }
            }

            else -> emptyList()
        }
    }

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
            if (hasLocationPermission) {
                try {
                    @SuppressLint("MissingPermission")
                    val locationTask = fusedLocationClient.lastLocation
                    locationTask.addOnSuccessListener { location ->
                        userLocation = location
                    }
                } catch (e: SecurityException) {
                    Log.e("BookMap", "Saknar rättighet för plats", e)
                }
            }
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
        BookGoogleMap(
            markers = currentMarkers,
            cameraPositionState = cameraPositionState,
            onMapLoaded = onMapLoaded,
            hasLocationPermission = hasLocationPermission,
            onMarkerClick = { bookId ->
                viewModel.onEvent(MapEvent.OnMarkerClick(bookId))
            },
        )

        MapActionButtons(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 192.dp, end = 16.dp),
            hasLocationPermission = hasLocationPermission,
            isFilterActive = state.mapFilter != MapFilter.ALL,
            onFilterClick = { viewModel.onEvent(MapEvent.OnToggleFilterDialog) },
            onMyLocationClick = {
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
            }
        )

        if (state.markersState is UiState.Loading) {
            FullScreenLoadingIndicator()
        }

        if (state.selectedMarker != null) {
            val distanceToMarker = if (userLocation != null) {
                getFormattedDistance(
                    userLat = userLocation!!.latitude,
                    userLng = userLocation!!.longitude,
                    markerLat = state.selectedMarker!!.latitude,
                    markerLng = state.selectedMarker!!.longitude
                )
            } else {
                null
            }

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
                    distanceText = distanceToMarker,
                    marker = state.selectedMarker!!,
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
                                state.selectedMarker!!.isFavorite
                            )
                        )
                    },
                    onToggleVisit = {
                        viewModel.onEvent(
                            MapEvent.OnToggleVisited(
                                state.selectedMarker!!.bookId,
                                state.selectedMarker!!.isVisited
                            )
                        )
                    }
                )
            }
        }

        if (state.showFilterDialog) {
            MapFilterDialog(
                currentFilter = state.mapFilter,
                onFilterSelected = { viewModel.onEvent(MapEvent.OnSetMapFilter(it)) },
                onDismiss = { viewModel.onEvent(MapEvent.OnToggleFilterDialog) }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}