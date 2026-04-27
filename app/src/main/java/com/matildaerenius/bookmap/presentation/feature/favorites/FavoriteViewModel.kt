package com.matildaerenius.bookmap.presentation.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.domain.model.FavoriteBook
import com.matildaerenius.bookmap.domain.usecase.ObserveFavoritesUseCase
import com.matildaerenius.bookmap.domain.usecase.RemoveFavoriteUseCase
import com.matildaerenius.bookmap.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<FavoriteBook>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<FavoriteBook>>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeFavoritesUseCase().collect { favoriteList ->
                _uiState.value = UiState.Success(favoriteList)
            }
        }
    }

    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.OnRemoveFavorite -> {
                viewModelScope.launch {
                    removeFavoriteUseCase(event.bookId)
                }
            }
        }
    }
}