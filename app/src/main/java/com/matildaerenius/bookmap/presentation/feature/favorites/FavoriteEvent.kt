package com.matildaerenius.bookmap.presentation.feature.favorites

sealed class FavoriteEvent {
    data class OnRemoveFavorite(val bookId: Int) : FavoriteEvent()
}