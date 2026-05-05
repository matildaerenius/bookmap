package com.matildaerenius.bookmap.data.mapper

import com.matildaerenius.bookmap.data.remote.dto.BookDto
import com.matildaerenius.bookmap.domain.model.Book

fun BookDto.toDomain(): Book {
    return Book(
        id = this.id,
        title = this.title ?: "Okänd titel",
        author = this.author ?: "Okänd författare",
        imageUrl = this.cover ?: "",
        ebook = this.ebooklength != null && this.ebooklength > 0,
        audio = this.audiobooklength != null && this.audiobooklength > 0
    )
}