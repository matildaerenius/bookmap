package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.model.Book
import com.matildaerenius.bookmap.domain.model.BookLocation
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.repository.BookRepository
import com.matildaerenius.bookmap.domain.repository.LocationRepository
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import com.matildaerenius.bookmap.core.DataError
import com.matildaerenius.bookmap.core.Resource
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

        val testBoundingBox = MapBoundingBox(
            northEastLat = 50.0,
            northEastLng = 5.0,
            southWestLat = 45.0,
            southWestLng = 0.0
        )

        val result = syncMapDataUseCase(testBoundingBox)

        assertTrue(result is Resource.Success)

        coVerify {
            markerRepository.upsertMarkers(match { markers ->
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

        val testBoundingBox = MapBoundingBox(50.0, 5.0, 45.0, 0.0)

        val result = syncMapDataUseCase(testBoundingBox)

        assertTrue(result is Resource.Error)
        coVerify(exactly = 0) { markerRepository.upsertMarkers(any()) }
    }

    @Test
    fun invoke_withOutOfBoundsLocation_doesNotFetchBooksOrSaveMarkers() = runTest {
        val fakeLocations = listOf(
            BookLocation(bookId = 100, locationName = "Paris", latitude = 48.8, longitude = 2.3, description = "A test")
        )
        coEvery { locationRepository.getLocations() } returns Resource.Success(fakeLocations)

        val stockholmBox = MapBoundingBox(
            northEastLat = 60.0,
            northEastLng = 19.0,
            southWestLat = 59.0,
            southWestLng = 17.0
        )

        val result = syncMapDataUseCase(stockholmBox)

        assertTrue(result is Resource.Success)
        coVerify(exactly = 0) { bookRepository.getBooksByIds(any()) }
        coVerify(exactly = 0) { markerRepository.upsertMarkers(any()) }
    }

    @Test
    fun invoke_withAntimeridianCrossing_includesCorrectLocations() = runTest {
        val fakeLocations = listOf(
            BookLocation(bookId = 200, locationName = "Fiji", latitude = -18.0, longitude = 178.0, description = "Island")
        )
        val fakeBooks = listOf(
            Book(id = 200, title = "Pacific Tales", author = "Ocean Writer", imageUrl = "url")
        )

        coEvery { locationRepository.getLocations() } returns Resource.Success(fakeLocations)
        coEvery { bookRepository.getBooksByIds(listOf(200)) } returns Resource.Success(fakeBooks)

        val pacificBox = MapBoundingBox(
            northEastLat = -10.0,
            northEastLng = -170.0,
            southWestLat = -20.0,
            southWestLng = 170.0
        )

        val result = syncMapDataUseCase(pacificBox)

        assertTrue(result is Resource.Success)
        coVerify {
            markerRepository.upsertMarkers(match { markers ->
                markers.size == 1 && markers.first().locationName == "Fiji"
            })
        }
    }
}