package com.matildaerenius.bookmap.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object Map : Routes
    @Serializable
    data class Detail(val bookId: Int) : Routes

}
