package com.matildaerenius.bookmap.data.repository

import com.matildaerenius.bookmap.data.remote.api.BookBeatApi
import com.matildaerenius.bookmap.data.remote.dto.BookDto
import com.matildaerenius.bookmap.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookRepositoryImplTest {

    private lateinit var api: BookBeatApi
    private lateinit var repository: BookRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = BookRepositoryImpl(api)
    }

    @Test
    fun `getBooksByIds returns Success with mapped data when API call succeeds`() = runTest {
        val bookIds = listOf(1765311)
        val mockDto = BookDto(
            id = 1765311,
            title = "Hell",
            author = "Test testsson",
            image = "url"
        )

        coEvery { api.getBookById(1765311) } returns mockDto

        val result = repository.getBooksByIds(bookIds)

        assertTrue("the result should be Resource.Success", result is Resource.Success)

        val successResult = result as Resource.Success
        assertEquals(1, successResult.data.size)
        assertEquals("Hell", successResult.data[0].title)
    }

    @Test
    fun `getBooksByIds returns Error when all API calls fail`() = runTest {
        val bookIds = listOf(1)

        coEvery { api.getBookById(1) } throws RuntimeException("Network Timeout")

        val result = repository.getBooksByIds(bookIds)

        assertTrue("The result should be Resource.Error because the list is empty", result is Resource.Error)

        val errorResult = result as Resource.Error
        assertEquals("Could not load books. Check your internet connection", errorResult.message)
    }
}