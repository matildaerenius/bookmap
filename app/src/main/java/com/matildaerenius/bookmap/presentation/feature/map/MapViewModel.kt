package com.matildaerenius.bookmap.presentation.feature.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.usecase.AddFavoriteUseCase
import com.matildaerenius.bookmap.domain.usecase.ObserveBookMarkersUseCase
import com.matildaerenius.bookmap.domain.usecase.ObserveFavoritesUseCase
import com.matildaerenius.bookmap.domain.usecase.RemoveFavoriteUseCase
import com.matildaerenius.bookmap.domain.usecase.SyncMapDataUseCase
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.core.DataError
import com.matildaerenius.bookmap.core.Resource
import com.matildaerenius.bookmap.core.UiText
import com.matildaerenius.bookmap.domain.usecase.ToggleVisitedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val syncMapDataUseCase: SyncMapDataUseCase,
    private val observeBookMarkersUseCase: ObserveBookMarkersUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val toggleVisitedUseCase: ToggleVisitedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()
    private val currentBounds = MutableStateFlow<MapBoundingBox?>(null)
    private var syncJob: Job? = null

    init {
        val initialBounds = MapConstants.STOCKHOLM_BOUNDS.toMapBoundingBox()
        currentBounds.value = initialBounds
        fetchMarkersForBounds(initialBounds)

        viewModelScope.launch {
            observeBookMarkersUseCase(currentBounds).collect { visibleMarkers ->
                _uiState.update { currentState ->

                    val shouldShowSuccess =
                        visibleMarkers.isNotEmpty() || currentState.markersState is UiState.Success

                    if (shouldShowSuccess) {
                        currentState.copy(markersState = UiState.Success(visibleMarkers))
                    } else {
                        currentState
                    }
                }
            }
        }
        viewModelScope.launch {
            observeFavoritesUseCase().collect { favs ->
                _uiState.update { currentState ->
                    currentState.copy(
                        favorites = favs
                    )
                }
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnMapBoundsChanged -> {
                currentBounds.value = event.boundingBox
                Log.d(
                    "BookMap",
                    "MapViewModel: Received new map bounds. Getting markers from Repository."
                )
                fetchMarkersForBounds(event.boundingBox)
            }

            is MapEvent.OnMarkerClick -> {
                val currentMarkers =
                    (_uiState.value.markersState as? UiState.Success)?.data ?: emptyList()
                val currentFavs = _uiState.value.favorites

                var clickedMarker = currentMarkers.find { it.bookId == event.bookId }
                if (clickedMarker == null) {
                    clickedMarker = currentFavs.find { it.bookId == event.bookId }?.marker
                }

                _uiState.update { currentState ->
                    currentState.copy(selectedMarker = clickedMarker)
                }
            }

            is MapEvent.OnDismissBottomSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(selectedMarker = null)
                }
            }

            is MapEvent.OnToggleFavorite -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedMarker = currentState.selectedMarker?.copy(
                            isFavorite = !event.isCurrentlyFavorite
                        )
                    )
                }
                viewModelScope.launch {
                    if (event.isCurrentlyFavorite) {
                        removeFavoriteUseCase(event.bookId)
                    } else {
                        addFavoriteUseCase(event.bookId)
                    }
                }
            }

            is MapEvent.OnToggleVisited -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedMarker = currentState.selectedMarker?.copy(
                            isVisited = !event.isCurrentlyVisited
                        )
                    )
                }

                viewModelScope.launch {
                    toggleVisitedUseCase(event.bookId, !event.isCurrentlyVisited)
                }
            }
        }
    }

    private fun fetchMarkersForBounds(bounds: MapBoundingBox) {
        syncJob?.cancel()

        syncJob = viewModelScope.launch {

            if (_uiState.value.markersState !is UiState.Success) {
                _uiState.update { it.copy(markersState = UiState.Loading) }
            }

            val result = syncMapDataUseCase(bounds)

            when (result) {
                is Resource.Success -> {
                    Log.d("BookMap", "SUCCESS! Found ${result.data.size} books in the area")
                    _uiState.update { currentState ->
                        if (currentState.markersState is UiState.Loading) {
                            currentState.copy(markersState = UiState.Success(emptyList()))
                        } else {
                            currentState
                        }
                    }
                }

                is Resource.Error -> {
                    if (_uiState.value.markersState !is UiState.Success) {
                        _uiState.update {
                            it.copy(markersState = UiState.Error(mapErrorToUiText(result.error)))
                        }
                    }
                    Log.e("BookMap", "ERROR! Network error: ${result.error}")
                }
            }
        }
    }

    private fun mapErrorToUiText(error: DataError): UiText {
        return when (error) {
            DataError.NETWORK_ERROR -> UiText.StringResource(R.string.error_network)
            else -> UiText.StringResource(R.string.error_unknown)
        }
    }
}