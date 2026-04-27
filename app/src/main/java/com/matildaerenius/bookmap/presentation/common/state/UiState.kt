package com.matildaerenius.bookmap.presentation.common.state

import com.matildaerenius.bookmap.util.UiText

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: UiText) : UiState<Nothing>
}
