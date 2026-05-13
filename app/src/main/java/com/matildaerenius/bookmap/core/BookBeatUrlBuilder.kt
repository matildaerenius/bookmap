package com.matildaerenius.bookmap.core

import java.text.Normalizer
import java.util.Locale

object BookBeatUrlBuilder {

    private const val BOOKBEAT_BOOK_BASE_URL = "https://www.bookbeat.com/se/book"

    fun createBookUrl(
        bookTitle: String,
        bookId: Int
    ): String {
        val slug = Normalizer.normalize(bookTitle, Normalizer.Form.NFD)
            .lowercase(Locale.ROOT)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .replace("[^a-z0-9]+".toRegex(), "-")
            .trim('-')
            .ifBlank { "book" }

        return "$BOOKBEAT_BOOK_BASE_URL/$slug-$bookId"
    }
}