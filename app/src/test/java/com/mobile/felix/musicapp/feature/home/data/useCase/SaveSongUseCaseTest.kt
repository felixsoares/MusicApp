package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveSongUseCaseTest {

    private val repository: HomeRepository = mockk()
    private lateinit var useCase: SaveSongUseCase

    @Before
    fun setUp() {
        useCase = SaveSongUseCase(repository)
    }

    @Test
    fun `invoke should delegate song to repository`() = runTest {
        coJustRun { repository.saveSong(fakeSong) }

        useCase.invoke(fakeSong)

        coVerify(exactly = 1) { repository.saveSong(fakeSong) }
    }
}

