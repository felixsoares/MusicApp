package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearLocalSongsUseCaseTest {

    private val repository: HomeRepository = mockk()
    private lateinit var useCase: ClearLocalSongsUseCase

    @Before
    fun setUp() {
        useCase = ClearLocalSongsUseCase(repository)
    }

    @Test
    fun `invoke should delegate to repository clearLocalSongs`() = runTest {
        coJustRun { repository.clearLocalSongs() }

        useCase.invoke()

        coVerify(exactly = 1) { repository.clearLocalSongs() }
    }
}

