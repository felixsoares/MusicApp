package com.mobile.felix.musicapp.feature.album.data.useCase

import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.album.domain.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAlbumUseCaseTest {

    private val repository: AlbumRepository = mockk()
    private lateinit var useCase: GetAlbumUseCase

    @Before
    fun setUp() {
        useCase = GetAlbumUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository returns songs`() = runTest {
        coEvery { repository.getAlbum(10L) } returns Result.Success(fakeSongList)

        val result = useCase.invoke(10L)

        assertEquals(Result.Success(fakeSongList), result)
        coVerify(exactly = 1) { repository.getAlbum(10L) }
    }

    @Test
    fun `invoke returns network error when repository fails`() = runTest {
        coEvery { repository.getAlbum(10L) } returns Result.Error(Failure.NetworkError)

        val result = useCase.invoke(10L)

        assertEquals(Result.Error(Failure.NetworkError), result)
    }

    @Test
    fun `invoke returns unknown error when repository returns unknown failure`() = runTest {
        coEvery { repository.getAlbum(10L) } returns Result.Error(Failure.Unknown)

        val result = useCase.invoke(10L)

        assertEquals(Result.Error(Failure.Unknown), result)
    }

    @Test
    fun `invoke delegates correct albumId to repository`() = runTest {
        val albumId = 99L
        coEvery { repository.getAlbum(albumId) } returns Result.Success(emptyList())

        useCase.invoke(albumId)

        coVerify { repository.getAlbum(albumId) }
    }
}

