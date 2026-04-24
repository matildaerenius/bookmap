package com.matildaerenius.bookmap.presentation.feature.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.R
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.usecase.AddFavoriteUseCase
import com.matildaerenius.bookmap.domain.usecase.ObserveBookMarkersUseCase
import com.matildaerenius.bookmap.domain.usecase.ObserveFavoritesUseCase
import com.matildaerenius.bookmap.domain.usecase.RemoveFavoriteUseCase
import com.matildaerenius.bookmap.domain.usecase.SyncMapDataUseCase
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import com.matildaerenius.bookmap.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val syncMapDataUseCase: SyncMapDataUseCase,
    private val observeBookMarkersUseCase: ObserveBookMarkersUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BookMapMarker>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BookMapMarker>>> = _uiState.asStateFlow()
    private val _selectedMarker = MutableStateFlow<BookMapMarker?>(null)
    val selectedMarker: StateFlow<BookMapMarker?> = _selectedMarker.asStateFlow()
    private val currentBounds = MutableStateFlow<MapBoundingBox?>(null)
    private var syncJob: Job? = null

    val favorites = observeFavoritesUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        val initialBounds = MapConstants.STOCKHOLM_BOUNDS.toMapBoundingBox()
        currentBounds.value = initialBounds
        fetchMarkersForBounds(initialBounds)

        viewModelScope.launch {
            observeBookMarkersUseCase(currentBounds).collect { visibleMarkers ->
                val currentState = _uiState.value

                if (visibleMarkers.isNotEmpty()) {
                    _uiState.value = UiState.Success(visibleMarkers)
                } else if (currentState is UiState.Success) {
                    _uiState.value = UiState.Success(emptyList())
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
                val currentState = _uiState.value
                if (currentState is UiState.Success) {
                    _selectedMarker.value = currentState.data.find { it.bookId == event.bookId }
                }
            }

            is MapEvent.OnDismissBottomSheet -> {
                _selectedMarker.value = null
            }

            is MapEvent.OnToggleFavorite -> {
                viewModelScope.launch {
                    if (event.isCurrentlyFavorite) {
                        removeFavoriteUseCase(event.bookId)
                    } else {
                        addFavoriteUseCase(event.bookId)
                    }
                }
            }
        }
    }

    private fun fetchMarkersForBounds(bounds: MapBoundingBox) {
        syncJob?.cancel()

        syncJob = viewModelScope.launch {
            if (_uiState.value !is UiState.Success) {
                _uiState.value = UiState.Loading
            }

            val result = syncMapDataUseCase(bounds)

            when (result) {
                is Resource.Success -> {
                    Log.d("BookMap", "2. SUCCESS! Found ${result.data.size} books in the area")
                }

                is Resource.Error -> {
                    if (_uiState.value !is UiState.Success) {
                        _uiState.value = UiState.Error(mapErrorToUiText(result.error))
                    }
                    Log.e("BookMap", "2. ERROR! Network error: ${result.error}")
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