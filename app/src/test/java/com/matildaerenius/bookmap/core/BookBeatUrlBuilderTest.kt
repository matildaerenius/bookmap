package com.matildaerenius.bookmap.core

import org.junit.Assert.assertEquals
import org.junit.Test

class BookBeatUrlBuilderTest {

    @Test
    fun `createBookUrl creates valid url for simple title`() {
        val result = BookBeatUrlBuilder.createBookUrl(
            bookTitle = "Röda rummet",
            bookId = 327
        )

        assertEquals(
            "https://www.bookbeat.com/se/book/roda-rummet-327",
            result
        )
    }

    @Test
    fun `createBookUrl removes Swedish diacritics`() {
        val result = BookBeatUrlBuilder.createBookUrl(
            bookTitle = "Män som hatar kvinnor",
            bookId = 123
        )

        assertEquals(
            "https://www.bookbeat.com/se/book/man-som-hatar-kvinnor-123",
            result
        )
    }

    @Test
    fun `createBookUrl replaces punctuation with hyphens`() {
        val result = BookBeatUrlBuilder.createBookUrl(
            bookTitle = "Hej, det är jag!",
            bookId = 456
        )

        assertEquals(
            "https://www.bookbeat.com/se/book/hej-det-ar-jag-456",
            result
        )
    }

    @Test
    fun `createBookUrl trims extra hyphens`() {
        val result = BookBeatUrlBuilder.createBookUrl(
            bookTitle = "--- Röda rummet ---",
            bookId = 327
        )

        assertEquals(
            "https://www.bookbeat.com/se/book/roda-rummet-327",
            result
        )
    }

    @Test
    fun `createBookUrl uses fallback slug for blank title`() {
        val result = BookBeatUrlBuilder.createBookUrl(
            bookTitle = "   ",
            bookId = 999
        )

        assertEquals(
            "https://www.bookbeat.com/se/book/book-999",
            result
        )
    }
}