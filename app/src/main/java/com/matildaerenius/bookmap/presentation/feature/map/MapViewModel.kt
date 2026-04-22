package com.matildaerenius.bookmap.presentation.feature.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import com.matildaerenius.bookmap.domain.usecase.SyncMapDataUseCase
import com.matildaerenius.bookmap.presentation.common.state.UiState
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val syncMapDataUseCase: SyncMapDataUseCase,
    private val markerRepository: MarkerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BookMapMarker>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BookMapMarker>>> = _uiState.asStateFlow()

    private var syncJob: Job? = null

    init {
        viewModelScope.launch {
            markerRepository.observeMarkers().collect { markers ->
                val currentState = _uiState.value

                if (markers.isNotEmpty()) {
                    _uiState.value = UiState.Success(markers)
                } else if (currentState is UiState.Success) {
                    _uiState.value = UiState.Success(emptyList())
                }
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnMapBoundsChanged ->  {
                Log.d("BookMap", "MapViewModel: Received new map bounds. Getting markers from Repository.")
                fetchMarkersForBounds(event.boundingBox)   }
            is MapEvent.OnMarkerClick -> {  }
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
                    _uiState.value = UiState.Success(result.data)
                    Log.d("BookMap", "2. SUCCESS! Found ${result.data.size} books in the area")
                }
                is Resource.Error -> {
                    if (_uiState.value !is UiState.Success) {
                        _uiState.value = UiState.Error(mapErrorToString(result.error))
                    }
                    Log.e("BookMap", "2. ERROR! Network error: ${result.error}")
                }
            }
        }
    }

    private fun mapErrorToString(error: DataError): String {
        return when (error) {
            DataError.NETWORK_ERROR -> "Networkerror. Shows saved locations."
            else -> "Ett oväntat fel uppstod."
        }
    }
}