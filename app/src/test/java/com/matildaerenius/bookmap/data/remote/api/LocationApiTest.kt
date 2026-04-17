package com.matildaerenius.bookmap.data.remote.api

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class LocationApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var locationApi: LocationApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val networkJson = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()

        locationApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .build()
            .create(LocationApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getBookLocations_parsesJsonList_andRequestsCorrectUrl() = runTest {
        val validJson = """
            [
              {
                "bookId": 123,
                "locationName": "Gamla Stan",
                "latitude": 59.325,
                "longitude": 18.070,
                "description": "En historisk plats i boken."
              }
            ]
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validJson)
        )

        val result = locationApi.getBookLocations()

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(123, result[0].bookId)

        val request = mockWebServer.takeRequest()
        assertEquals("/matildaerenius/6412034116187abb7f8c8699333695d4/raw/book_locations.json", request.path)
    }

    @Test
    fun getBookLocations_handlesEmptyList() = runTest {
        val emptyJsonList = "[]"

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(emptyJsonList)
        )

        val result = locationApi.getBookLocations()

        assertNotNull(result)
        assertEquals(0, result.size)
    }
}