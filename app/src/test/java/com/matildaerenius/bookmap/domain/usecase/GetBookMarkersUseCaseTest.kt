package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.model.BookLocation
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetBookMarkersUseCaseTest {

    private lateinit var mockLocationRepository: LocationRepository
    private lateinit var mockBookRepository: BookRepository
    private lateinit var useCase: GetBookMarkersUseCase

    @Before
    fun setUp() {
        mockLocationRepository = mockk()
        mockBookRepository = mockk()
        useCase = GetBookMarkersUseCase(mockLocationRepository, mockBookRepository)
    }

    @Test
    fun `invoke returns Success with combined data when both APIs succeed`() = runTest {
        val mockLocations = listOf(
            BookLocation(
                bookId = 1,
                locationName = "Gamla Stan",
                latitude = 59.32,
                longitude = 18.07,
                description = "En spännande gränd"
            )
        )
        val mockBooks = listOf(
            Book(
                id = 1,
                title = "Test",
                author = "Test testsson",
                imageUrl = "https://test.com/image.jpg"
            )
        )

        coEvery { mockLocationRepository.getLocations() } returns Resource.Success(mockLocations)
        coEvery { mockBookRepository.getBooksByIds(listOf(1)) } returns Resource.Success(mockBooks)

        val result = useCase()

        assertTrue(result is Resource.Success)

        val successResult = result as Resource.Success
        val markers = successResult.data

        assertEquals(1, markers.size)
        assertEquals("Gamla Stan", markers.first().locationName)
        assertEquals("Test", markers.first().bookTitle)
    }

    @Test
    fun `invoke returns Error when fetching locations fails`() = runTest {
        coEvery { mockLocationRepository.getLocations() } returns Resource.Error("Could not load position data")

        val result = useCase()

        assertTrue(result is Resource.Error)
        val errorResult = result as Resource.Error
        assertEquals("Could not load position data", errorResult.message)
    }

    @Test
    fun `invoke returns Error when fetching books fails`() = runTest {
        val mockLocations = listOf(
            BookLocation(1, "Gamla Stan", 59.32, 18.07, "des")
        )
        coEvery { mockLocationRepository.getLocations() } returns Resource.Success(mockLocations)
        coEvery { mockBookRepository.getBooksByIds(listOf(1)) } returns Resource.Error("Could not load books")

        val result = useCase()

        assertTrue(result is Resource.Error)
        val errorResult = result as Resource.Error
        assertEquals("Could not load books", errorResult.message)
    }
    @Test
    fun `invoke returns Success with empty list when locations are empty`() = runTest {
        coEvery { mockLocationRepository.getLocations() } returns Resource.Success(emptyList())

        val result = useCase()

        assertTrue(result is Resource.Success)
        val successResult = result as Resource.Success
        assertTrue(successResult.data.isEmpty())
    }

    @Test
    fun `invoke returns only markers for which books were found`() = runTest {
        val mockLocations = listOf(
            BookLocation(1, "Gamla Stan", 59.32, 18.07, "Beskrivning 1"),
            BookLocation(2, "Södermalm", 59.31, 18.06, "Beskrivning 2")
        )
        val mockBooks = listOf(
            Book(1, "Test", "test testsspn", "url")
        )

        coEvery { mockLocationRepository.getLocations() } returns Resource.Success(mockLocations)
        coEvery { mockBookRepository.getBooksByIds(listOf(1, 2)) } returns Resource.Success(mockBooks)

        val result = useCase()

        assertTrue(result is Resource.Success)
        val successResult = result as Resource.Success
        val markers = successResult.data

        assertEquals(1, markers.size)
        assertEquals(1, markers.first().bookId)
        assertEquals("Gamla Stan", markers.first().locationName)
    }
}