package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.model.BookLocation
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import com.matildaerenius.bookmap.util.DataError
import com.matildaerenius.bookmap.util.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SyncMapDataUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var markerRepository: MarkerRepository
    private lateinit var syncMapDataUseCase: SyncMapDataUseCase

    @Before
    fun setup() {
        locationRepository = mockk(relaxed = true)
        bookRepository = mockk(relaxed = true)
        markerRepository = mockk(relaxed = true)

        syncMapDataUseCase = SyncMapDataUseCase(
            locationRepository,
            bookRepository,
            markerRepository
        )
    }

    @Test
    fun invoke_withSuccessfulNetworkCalls_savesMergedDataToCache() = runTest {
        val fakeLocations = listOf(
            BookLocation(bookId = 100, locationName = "Paris", latitude = 48.8, longitude = 2.3, description = "A test")
        )
        val fakeBooks = listOf(
            Book(id = 100, title = "The Paris Novel", author = "Jane Doe", imageUrl = "url")
        )

        coEvery { locationRepository.getLocations() } returns Resource.Success(fakeLocations)
        coEvery { bookRepository.getBooksByIds(listOf(100)) } returns Resource.Success(fakeBooks)

        val result = syncMapDataUseCase()

        assertTrue(result is Resource.Success)

        coVerify {
            markerRepository.replaceCache(match { markers ->
                markers.size == 1 &&
                        markers.first().bookId == 100 &&
                        markers.first().locationName == "Paris" &&
                        markers.first().bookTitle == "The Paris Novel"
            })
        }
    }

    @Test
    fun invoke_whenLocationApiFails_returnsErrorAndDoesNotSave() = runTest {
        coEvery { locationRepository.getLocations() } returns Resource.Error(DataError.NETWORK_ERROR)

        val result = syncMapDataUseCase()

        assertTrue(result is Resource.Error)
        coVerify(exactly = 0) { markerRepository.replaceCache(any()) }
    }
}