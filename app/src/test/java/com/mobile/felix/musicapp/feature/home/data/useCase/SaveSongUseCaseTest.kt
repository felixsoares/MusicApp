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
    fun `invoke should delegate trackId to repository`() = runTest {
        val trackId = fakeSong.trackId!!
        coJustRun { repository.saveSongToDetailsCache(trackId) }

        useCase.invoke(trackId)

        coVerify(exactly = 1) { repository.saveSongToDetailsCache(trackId) }
    }
}
