package com.matildaerenius.bookmap.data.repository

import app.cash.turbine.test
import com.matildaerenius.bookmap.data.local.dao.FavoriteDao
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import com.matildaerenius.bookmap.data.local.entity.FavoriteWithVisit
import com.matildaerenius.bookmap.domain.model.BookMapMarker
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
        val favoriteEntity = FavoriteEntity(
            bookId = 1,
            savedAt = 1000L,
            title = "Test Book",
            author = "Test Author",
            locationName = "Stockholm",
            imageUrl = "url",
            ebook = true,
            audio = true
        )

        val favoriteWithVisit = FavoriteWithVisit(
            favorite = favoriteEntity,
            isVisited = true
        )

        every { favoriteDao.getFavoritesWithDetails() } returns flowOf(listOf(favoriteWithVisit))

        favoriteRepository.getFavorites().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(1, emittedList.first().bookId)
            assertEquals("Test Book", emittedList.first().marker?.bookTitle)
            assertTrue(emittedList.first().marker?.isVisited == true)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getFavorites_mapsNotVisitedCorrectly() = runTest {
        val favoriteEntity = FavoriteEntity(
            bookId = 2,
            savedAt = 2000L,
            title = "Another Book",
            author = "Another Author",
            locationName = "Göteborg",
            imageUrl = "url2",
            ebook = false,
            audio = false
        )

        val relation = FavoriteWithVisit(
            favorite = favoriteEntity,
            isVisited = false
        )

        every { favoriteDao.getFavoritesWithDetails() } returns flowOf(listOf(relation))

        favoriteRepository.getFavorites().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(2, emittedList.first().bookId)
            assertFalse(emittedList.first().marker?.isVisited == true)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun addFavorite_mapsToEntityAndCallsDao() = runTest {
        val testSavedAt = 1000L
        val marker = BookMapMarker(
            bookId = 1,
            bookTitle = "test",
            bookAuthor = "test testsson",
            description = "Desc",
            bookImageUrl = "url",
            locationName = "norrmalm",
            latitude = 0.0,
            longitude = 0.0,
            ebook = true,
            audio = true,
            isFavorite = false,
            isVisited = false
        )

        favoriteRepository.addFavorite(marker, testSavedAt)

        val expectedEntity = FavoriteEntity(
            bookId = 1,
            savedAt = testSavedAt,
            title = "test",
            author = "test testsson",
            locationName = "norrmalm",
            imageUrl = "url",
            ebook = true,
            audio = true
        )

        coVerify { favoriteDao.insertFavorite(expectedEntity) }
    }

    @Test
    fun removeFavorite_callsDaoWithCorrectId() = runTest {
        val testBookId = 123

        favoriteRepository.removeFavorite(testBookId)

        coVerify { favoriteDao.deleteFavorite(testBookId) }
    }
}