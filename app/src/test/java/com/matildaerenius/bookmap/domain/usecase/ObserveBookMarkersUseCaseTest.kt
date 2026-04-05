package com.matildaerenius.bookmap.domain.usecase

import app.cash.turbine.test
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun invoke_returnsFlowOfMarkersFromRepository() = runTest {
        val fakeMarker = BookMapMarker(
            bookId = 1,
            locationName = "Stockholm",
            latitude = 59.32,
            longitude = 18.06,
            description = "Test",
            bookTitle = "Test Book",
            bookAuthor = "Author",
            bookImageUrl = "url"
        )

        every { markerRepository.observeMarkers() } returns flowOf(listOf(fakeMarker))

        observeBookMarkersUseCase().test {
            val list = awaitItem()
            assertEquals("Stockholm", list.first().locationName)
            cancelAndIgnoreRemainingEvents()        }
    }
}