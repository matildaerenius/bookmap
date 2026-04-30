package com.matildaerenius.bookmap.domain.usecase

import app.cash.turbine.test
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.model.MapBoundingBox
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ObserveBookMarkersUseCaseTest {

    private lateinit var markerRepository: MarkerRepository
    private lateinit var observeBookMarkersUseCase: ObserveBookMarkersUseCase

    @Before
    fun setup() {
        markerRepository = mockk(relaxed = true)
        observeBookMarkersUseCase = ObserveBookMarkersUseCase(markerRepository)
    }

    @Test
    fun `invoke returns only markers within given bounding box`() = runTest {
        val stockholmMarker = BookMapMarker(
            bookId = 1,
            locationName = "Stockholm",
            latitude = 59.32,
            longitude = 18.06,
            description = "Test",
            bookTitle = "Bok 1",
            bookAuthor = "Författare",
            bookImageUrl = "url",
            isVisited = false,
            isFavorite = false
        )

        val gothenburgMarker = BookMapMarker(
            bookId = 2,
            locationName = "Göteborg",
            latitude = 57.70,
            longitude = 11.97,
            description = "Test",
            bookTitle = "Bok 2",
            bookAuthor = "Författare",
            bookImageUrl = "url",
            isVisited = false,
            isFavorite = false
        )

        every { markerRepository.observeMarkers() } returns flowOf(
            listOf(
                stockholmMarker,
                gothenburgMarker
            )
        )

        val stockholmBounds = MapBoundingBox(
            southWestLat = 59.00, southWestLng = 17.50,
            northEastLat = 59.50, northEastLng = 18.50
        )
        val boundsFlow = flowOf(stockholmBounds)

        observeBookMarkersUseCase(boundsFlow).test {
            val list = awaitItem()
            assertEquals(1, list.size)
            assertEquals("Stockholm", list.first().locationName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke returns empty list when bounds are null`() = runTest {
        val fakeMarker = BookMapMarker(
            bookId = 1,
            locationName = "Stockholm",
            latitude = 59.32,
            longitude = 18.06,
            description = "Test",
            bookTitle = "Test Book",
            bookAuthor = "Author",
            bookImageUrl = "url",
            isVisited = false,
            isFavorite = false
        )

        every { markerRepository.observeMarkers() } returns flowOf(listOf(fakeMarker))

        val nullBoundsFlow = flowOf<MapBoundingBox?>(null)

        observeBookMarkersUseCase(nullBoundsFlow).test {
            val list = awaitItem()
            assertTrue("Listan ska vara tom om bounds är null", list.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}