package com.matildaerenius.bookmap.domain.usecase

import com.matildaerenius.bookmap.domain.repository.MarkerRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleVisitedUseCaseTest {

    private lateinit var repository: MarkerRepository
    private lateinit var toggleVisitedUseCase: ToggleVisitedUseCase

    @Before
    fun setUp() {

        repository = mockk(relaxed = true)

        toggleVisitedUseCase = ToggleVisitedUseCase(repository)
    }

    @Test
    fun `invoke should call updateVisitedStatus on repository with correct parameters`() = runTest {
        val testBookId = 42
        val testIsVisited = true

        toggleVisitedUseCase(testBookId, testIsVisited)

        coVerify {
            repository.updateVisitedStatus(testBookId, testIsVisited)
        }
    }
}