package com.matildaerenius.bookmap.presentation.feature.map

import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.FavoriteBook
import com.matildaerenius.bookmap.presentation.common.state.UiState

data class MapUiState(
    val markersState: UiState<List<BookMapMarker>> = UiState.Loading,
    val selectedMarker: BookMapMarker? = null,
    val favorites: List<FavoriteBook> = emptyList()
)