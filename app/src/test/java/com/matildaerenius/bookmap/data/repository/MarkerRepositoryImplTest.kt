package com.matildaerenius.bookmap.data.repository

import app.cash.turbine.test
import com.matildaerenius.bookmap.data.local.dao.MarkerDao
import com.matildaerenius.bookmap.data.local.dao.VisitedDao
import com.matildaerenius.bookmap.data.local.entity.BookWithDetails
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MarkerRepositoryImplTest {

    private lateinit var markerDao: MarkerDao

    private lateinit var visitedDao: VisitedDao
    private lateinit var markerRepository: MarkerRepositoryImpl

    @Before
    fun setup() {
        markerDao = mockk(relaxed = true)
        visitedDao = mockk(relaxed = true)
        markerRepository = MarkerRepositoryImpl(markerDao, visitedDao)
    }

    @Test
    fun observeMarkers_mapsEntitiesToDomainModels_andEmitsProperly() = runTest {
        val entity = MarkerEntity(
            bookId = 1,
            title = "Test Title",
            author = "Test Author",
            description = "Test Description",
            coverImageUrl = "url",
            locationDescription = "Stockholm",
            latitude = 59.32,
            longitude = 18.06
        )

        val bookWithDetails = BookWithDetails(
            marker = entity,
            visited = null,
            favorite = null
        )

        every { markerDao.getMarkersWithDetails() } returns flowOf(listOf(bookWithDetails))

        markerRepository.observeMarkers().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(1, emittedList.first().bookId)
            assertEquals("Test Title", emittedList.first().bookTitle)

            assertEquals(false, emittedList.first().isVisited)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun upsertMarkers_mapsDomainModelsToEntities_andCallsDao() = runTest {
        val domainMarker = BookMapMarker(
            bookId = 1,
            bookTitle = "Test Title",
            bookAuthor = "Test Author",
            description = "Test Description",
            bookImageUrl = "url",
            locationName = "Stockholm",
            latitude = 59.32,
            longitude = 18.06,
            isVisited = false,
            isFavorite = false
        )

        markerRepository.upsertMarkers(listOf(domainMarker))

        coVerify {
            markerDao.upsertMarkers(match { list ->
                list.size == 1 &&
                        list.first().bookId == 1 &&
                        list.first().title == "Test Title"
            })
        }
    }
}