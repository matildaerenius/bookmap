package com.matildaerenius.bookmap.data.mapper

import com.matildaerenius.bookmap.data.remote.dto.BookDto
import org.junit.Assert.assertEquals
import org.junit.Test

class BookMapperTest {

    @Test
    fun `toDomain maps valid DTO correctly`() {
        val dto = BookDto(id = 1, title = "Hell", author = "Test testsson", image = "url_to_image")

        val result = dto.toDomain()

        assertEquals(1, result.id)
        assertEquals("Hell", result.title)
        assertEquals("Test testsson", result.author)
        assertEquals("url_to_image", result.imageUrl)
    }

    @Test
    fun `toDomain handles null values safely`() {
        val dto = BookDto(id = 2, title = null, author = null, image = null)

        val result = dto.toDomain()

        assertEquals(2, result.id)
        assertEquals("Okänd titel", result.title)
        assertEquals("Okänd författare", result.author)
        assertEquals("", result.imageUrl)
    }
}