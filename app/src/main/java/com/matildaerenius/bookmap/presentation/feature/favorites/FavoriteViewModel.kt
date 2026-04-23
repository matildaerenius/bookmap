package com.matildaerenius.bookmap.presentation.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matildaerenius.bookmap.domain.model.FavoriteBook
import com.matildaerenius.bookmap.domain.usecase.ObserveFavoritesUseCase
import com.matildaerenius.bookmap.domain.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteBook>> = observeFavoritesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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