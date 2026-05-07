package com.matildaerenius.bookmap.presentation.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.domain.usecase.ObserveFavoritesUseCase
import com.matildaerenius.bookmap.domain.usecase.RemoveAllFavoritesUseCase
import com.matildaerenius.bookmap.domain.usecase.RemoveFavoriteUseCase
import com.matildaerenius.bookmap.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val removeAllFavoritesUseCase: RemoveAllFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeFavoritesUseCase().collect { favoriteList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        favoritesState = UiState.Success(favoriteList)
                    )
                }
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

            is FavoriteEvent.OnRemoveAllFavorites -> {
                viewModelScope.launch {
                    removeAllFavoritesUseCase()
                }
            }
        }
    }
}