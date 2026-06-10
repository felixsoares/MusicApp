package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLocalSongsUseCaseTest {

    private val repository: HomeRepository = mockk()
    private lateinit var useCase: GetLocalSongsUseCase

    @Before
    fun setUp() {
        useCase = GetLocalSongsUseCase(repository)
    }

    @Test
    fun `invoke should return success with local songs`() = runTest {
        coEvery { repository.getLocalSongs() } returns Result.Success(fakeSongList)

        val result = useCase.invoke()

        assertEquals(Result.Success(fakeSongList), result)
        coVerify(exactly = 1) { repository.getLocalSongs() }
    }

    @Test
    fun `invoke should return success with empty list when no songs`() = runTest {
        coEvery { repository.getLocalSongs() } returns Result.Success(emptyList())

        val result = useCase.invoke()

        assertEquals(Result.Success(emptyList<Nothing>()), result)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        coEvery { repository.getLocalSongs() } returns Result.Error(Failure.Unknown)

        val result = useCase.invoke()

        assertEquals(Result.Error(Failure.Unknown), result)
    }
}

