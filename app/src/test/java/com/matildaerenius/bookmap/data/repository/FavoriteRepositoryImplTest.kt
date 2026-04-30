package com.matildaerenius.bookmap.data.repository

import app.cash.turbine.test
import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.entity.FavoriteBookRelation
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.data.local.entity.MarkerEntity
import com.matildaerenius.bookmap.data.local.entity.VisitedEntity
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryImplTest {

    private lateinit var favoriteDao: FavoriteDao
    private lateinit var favoriteRepository: FavoriteRepositoryImpl

    @Before
    fun setup() {
        favoriteDao = mockk(relaxed = true)
        favoriteRepository = FavoriteRepositoryImpl(favoriteDao)
    }

    @Test
    fun getFavorites_mapsRelationsToDomainModels() = runTest {
        val favoriteEntity = FavoriteEntity(bookId = 1, savedAt = 1000L)
        val markerEntity = MarkerEntity(
            bookId = 1,
            title = "Test Book",
            author = "Test Author",
            description = "Desc",
            coverImageUrl = "url",
            locationDescription = "Stockholm",
            latitude = 59.32,
            longitude = 18.06
        )
        val visitedEntity = VisitedEntity(bookId = 1)
        val relation = FavoriteBookRelation(favorite = favoriteEntity, marker = markerEntity, visited = visitedEntity)

        every { favoriteDao.getFavoritesWithDetails() } returns flowOf(listOf(relation))

        favoriteRepository.getFavorites().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(1, emittedList.first().bookId)
            assertEquals("Test Book", emittedList.first().marker?.bookTitle)

            cancelAndIgnoreRemainingEvents()        }
    }

    @Test
    fun getFavorites_handlesNullMarkerProperly() = runTest {
        val favoriteEntity = FavoriteEntity(bookId = 2, savedAt = 2000L)
        val relation = FavoriteBookRelation(favorite = favoriteEntity, marker = null, visited = null)

        every { favoriteDao.getFavoritesWithDetails() } returns flowOf(listOf(relation))

        favoriteRepository.getFavorites().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(2, emittedList.first().bookId)
            assertNull(emittedList.first().marker)

            cancelAndIgnoreRemainingEvents()        }
    }

    @Test
    fun addFavorite_mapsToEntityAndCallsDao() = runTest {
        val testBookId = 123
        val testSavedAt = 1500L

        favoriteRepository.addFavorite(testBookId, testSavedAt)

        val expectedEntity = FavoriteEntity(bookId = testBookId, savedAt = testSavedAt)
        coVerify { favoriteDao.insertFavorite(expectedEntity) }
    }

    @Test
    fun removeFavorite_callsDaoWithCorrectId() = runTest {
        val testBookId = 123

        favoriteRepository.removeFavorite(testBookId)

        coVerify { favoriteDao.deleteFavorite(testBookId) }
    }
}