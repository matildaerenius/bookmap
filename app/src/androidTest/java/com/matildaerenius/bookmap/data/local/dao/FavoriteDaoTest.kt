package com.matildaerenius.bookmap.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.matildaerenius.bookmap.data.local.BookMapDatabase
import com.matildaerenius.bookmap.data.local.entity.FavoriteEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest {

    private lateinit var database: BookMapDatabase
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            BookMapDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        favoriteDao = database.favoriteDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertFavorite_savesToDatabase_andEmitsToFlow() = runTest {
        val testBookId = "book_123"
        val favorite = FavoriteEntity(bookId = testBookId, savedAt = 1000L)

        favoriteDao.insertFavorite(favorite)

        favoriteDao.getAllFavorites().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(testBookId, emittedList.first().bookId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteFavorite_removesFromDatabase() = runTest {
        val testBookId = "book_123"
        val favorite = FavoriteEntity(bookId = testBookId, savedAt = 1000L)
        favoriteDao.insertFavorite(favorite)

        favoriteDao.deleteFavorite(testBookId)

        favoriteDao.getAllFavorites().test {
            val emittedList = awaitItem()

            assertEquals(0, emittedList.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}