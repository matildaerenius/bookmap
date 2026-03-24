package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.remote.api.LocationApi
import com.matildaerenius.bookmap.data.remote.dto.LocationDto
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocationRepositoryImplTest {

    private lateinit var mockApi: LocationApi
    private lateinit var repository: LocationRepositoryImpl

    @Before
    fun setUp() {
        mockApi = mockk()
        repository = LocationRepositoryImpl(mockApi)
    }

    @Test
    fun `getLocations returns Success when API call is successful`() = runTest {
        val mockList = listOf(
            LocationDto(1, "Gamla Stan", 59.32, 18.07, "des")
        )
        coEvery { mockApi.getBookLocations() } returns mockList

        val result = repository.getLocations()

        assertTrue(result is Resource.Success)

        val successResult = result as Resource.Success
        assertEquals("Gamla Stan", successResult.data.first().locationName)
    }

    @Test
    fun `getLocations returns Error when API call fails`() = runTest {
        coEvery { mockApi.getBookLocations() } throws Exception("Network error: Timeout")

        val result = repository.getLocations()

        assertTrue(result is Resource.Error)

        val errorResult = result as Resource.Error
        assertEquals(DataError.PARSING_ERROR, errorResult.error)
    }
}