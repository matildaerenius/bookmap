package com.matildaerenius.bookmap.data.remote.dto

import kotlinx.serialization.Serializable
@Serializable
data class BookDto(
    val id: Int,
    val title: String?,
    val author: String?,
    val image: String?
)