package com.matildaerenius.bookmap.domain.model

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val imageUrl: String,
    val ebook: Boolean,
    val audio: Boolean
)