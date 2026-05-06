package com.matildaerenius.bookmap.presentation.feature.map

import com.matildaerenius.bookmap.domain.model.MapBoundingBox

sealed interface MapEvent {
    data class OnMapBoundsChanged(val boundingBox: MapBoundingBox) : MapEvent
    data class OnMarkerClick(val bookId: Int) : MapEvent
    data object OnDismissBottomSheet : MapEvent
    data class OnToggleFavorite(val bookId: Int, val isCurrentlyFavorite: Boolean) : MapEvent
    data class OnToggleVisited(val bookId: Int, val isCurrentlyVisited: Boolean) : MapEvent

    object OnToggleFilterDialog : MapEvent

    data class OnSetMapFilter(val filter: MapFilter) : MapEvent
}
