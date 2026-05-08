package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoveAllFavoritesUseCaseTest {

    private lateinit var repository: FavoriteRepository

    private lateinit var useCase: RemoveAllFavoritesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = RemoveAllFavoritesUseCase(repository)
    }

    @Test
    fun `invoke should call removeAllFavorites in repository exactly once`() = runTest {
        coEvery { repository.removeAllFavorites() } returns Unit

        useCase()

        coVerify(exactly = 1) { repository.removeAllFavorites() }
    }
}