package com.matildaerenius.bookmap.presentation.feature.details

sealed interface DetailEvent {
    data object OnRetryClick : DetailEvent
    data object OnToggleFavorite : DetailEvent
}