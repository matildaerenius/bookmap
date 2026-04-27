package com.matildaerenius.bookmap.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Main : Routes
}
