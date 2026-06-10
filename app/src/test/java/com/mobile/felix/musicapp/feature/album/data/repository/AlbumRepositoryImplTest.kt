package com.mobile.felix.musicapp.feature.album.data.repository

import com.mobile.felix.musicapp.TestFixtures.fakeAlbumResponseList
import com.mobile.felix.musicapp.TestFixtures.fakeAlbumSongsAfterDrop
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.album.domain.source.AlbumDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class AlbumRepositoryImplTest {

    private val dataSource: AlbumDataSource = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AlbumRepositoryImpl

    @Before
    fun setUp() {
        repository = AlbumRepositoryImpl(dataSource, testDispatcher)
    }

    @Test
    fun `getAlbum returns success dropping first album item`() = runTest(testDispatcher) {
        coEvery { dataSource.getAlbum(10L) } returns fakeAlbumResponseList

        val result = repository.getAlbum(10L)

        assertEquals(Result.Success(fakeAlbumSongsAfterDrop), result)
    }

    @Test
    fun `getAlbum returns success with empty list when response has only album item`() = runTest(testDispatcher) {
        coEvery { dataSource.getAlbum(10L) } returns fakeAlbumResponseList.take(1)

        val result = repository.getAlbum(10L)

        assertEquals(Result.Success(emptyList<Nothing>()), result)
    }

    @Test
    fun `getAlbum returns NetworkError when IOException is thrown`() = runTest(testDispatcher) {
        coEvery { dataSource.getAlbum(any()) } throws IOException("No internet")

        val result = repository.getAlbum(10L)

        assertEquals(Result.Error(Failure.NetworkError), result)
    }

    @Test
    fun `getAlbum returns Unknown error when generic exception is thrown`() = runTest(testDispatcher) {
        coEvery { dataSource.getAlbum(any()) } throws RuntimeException("Unexpected error")

        val result = repository.getAlbum(10L)

        assertEquals(Result.Error(Failure.Unknown), result)
    }
}

