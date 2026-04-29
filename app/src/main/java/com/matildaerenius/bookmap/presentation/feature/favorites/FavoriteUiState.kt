package com.matildaerenius.bookmap.presentation.feature.favorites

import com.matildaerenius.bookmap.domain.model.FavoriteBook
import com.matildaerenius.bookmap.presentation.common.state.UiState

data class FavoriteUiState(
    val favoritesState: UiState<List<FavoriteBook>> = UiState.Loading
)