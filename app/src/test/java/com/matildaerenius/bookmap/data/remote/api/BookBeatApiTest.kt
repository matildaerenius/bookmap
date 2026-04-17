package com.matildaerenius.bookmap.data.remote.api

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class BookBeatApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var bookBeatApi: BookBeatApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val networkJson = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()

        bookBeatApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(BookBeatApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getBookById_parsesJson_andRequestsCorrectUrl() = runTest {
        val validJson = """
            {
              "id": 123,
              "title": "Clean Architecture",
              "author": "Robert C. Martin",
              "cover": "https://example.com/cover.jpg"
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validJson)
        )

        val requestedId = 123
        val result = bookBeatApi.getBookById(requestedId)

        assertNotNull(result)
        assertEquals(123, result.id)
        assertEquals("Clean Architecture", result.title)
        assertEquals("Robert C. Martin", result.author)
        assertEquals("https://example.com/cover.jpg", result.cover)

        val request = mockWebServer.takeRequest()
        assertEquals("/api/books/123", request.path)
    }

    @Test
    fun getBookById_handlesMissingOptionalFields() = runTest {
        val jsonMissingFields = """
            {
              "id": 101
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonMissingFields)
        )

        val result = bookBeatApi.getBookById(101)

        assertEquals(101, result.id)
        assertNull(result.title)
        assertNull(result.author)
        assertNull(result.cover)
    }
}