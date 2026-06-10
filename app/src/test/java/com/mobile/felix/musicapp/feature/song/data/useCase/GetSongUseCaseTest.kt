package com.mobile.felix.musicapp.feature.song.data.useCase

import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.song.domain.repository.SongRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSongUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var useCase: GetSongUseCase

    @Before
    fun setUp() {
        useCase = GetSongUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns song`() = runTest {
        coEvery { repository.getSong(1L) } returns Result.Success(fakeSong)

        val result = useCase.invoke(1L)

        assertEquals(Result.Success(fakeSong), result)
        coVerify(exactly = 1) { repository.getSong(1L) }
    }

    @Test
    fun `invoke returns error when repository fails`() = runTest {
        coEvery { repository.getSong(1L) } returns Result.Error(Failure.Unknown)

        val result = useCase.invoke(1L)

        assertEquals(Result.Error(Failure.Unknown), result)
    }

    @Test
    fun `invoke delegates correct id to repository`() = runTest {
        val id = 42L
        coEvery { repository.getSong(id) } returns Result.Success(fakeSong)

        useCase.invoke(id)

        coVerify { repository.getSong(id) }
    }
}

