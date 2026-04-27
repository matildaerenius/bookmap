package com.matildaerenius.bookmap.presentation.feature.favorites

sealed interface FavoriteEvent {
    data class OnRemoveFavorite(val bookId: Int) : FavoriteEvent
}