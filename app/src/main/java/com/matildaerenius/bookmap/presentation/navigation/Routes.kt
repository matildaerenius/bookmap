package com.matildaerenius.bookmap.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object Onboarding : Routes
    @Serializable
    data object Map : Routes
}
