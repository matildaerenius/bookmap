package com.matildaerenius.bookmap.data.remote.dto

import kotlinx.serialization.Serializable
@Serializable
data class BookDto(
    val id: Int,
    val title: String? = null,
    val author: String? = null,
    val cover: String? = null
)