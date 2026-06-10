package com.mobile.felix.musicapp.feature.home.data.repository

import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import com.mobile.felix.musicapp.feature.home.domain.source.HomeRemoteDataSource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class HomeRepositoryImplTest {

    private val remoteDataSource: HomeRemoteDataSource = mockk()
    private val localDataSource: HomeLocalDataSource = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: HomeRepositoryImpl

    @Before
    fun setUp() {
        repository = HomeRepositoryImpl(remoteDataSource, localDataSource, testDispatcher)
    }


    @Test
    fun `getSongsByTerm returns success when remote data source returns songs`() = runTest(testDispatcher) {
        coEvery { remoteDataSource.getSongsByTerm("rock") } returns fakeSongList

        val result = repository.getSongsByTerm("rock")

        assertEquals(Result.Success(fakeSongList), result)
    }

    @Test
    fun `getSongsByTerm returns NetworkError when IOException is thrown`() = runTest(testDispatcher) {
        coEvery { remoteDataSource.getSongsByTerm(any()) } throws IOException("No internet")

        val result = repository.getSongsByTerm("rock")

        assertEquals(Result.Error(Failure.NetworkError), result)
    }

    @Test
    fun `getSongsByTerm returns Unknown error when generic exception is thrown`() = runTest(testDispatcher) {
        coEvery { remoteDataSource.getSongsByTerm(any()) } throws RuntimeException("Unexpected")

        val result = repository.getSongsByTerm("rock")

        assertEquals(Result.Error(Failure.Unknown), result)
    }


    @Test
    fun `saveSong delegates to local data source`() = runTest(testDispatcher) {
        coJustRun { localDataSource.saveSong(fakeSong) }

        repository.saveSong(fakeSong)

        coVerify(exactly = 1) { localDataSource.saveSong(fakeSong) }
    }


    @Test
    fun `getLocalSongs returns success with songs from local data source`() = runTest(testDispatcher) {
        coEvery { localDataSource.getSongs() } returns fakeSongList

        val result = repository.getLocalSongs()

        assertEquals(Result.Success(fakeSongList), result)
    }

    @Test
    fun `getLocalSongs returns success with empty list when local data source returns null`() = runTest(testDispatcher) {
        coEvery { localDataSource.getSongs() } returns null

        val result = repository.getLocalSongs()

        assertEquals(Result.Success(emptyList<Nothing>()), result)
    }

    @Test
    fun `getLocalSongs returns success with empty list when local data source returns empty`() = runTest(testDispatcher) {
        coEvery { localDataSource.getSongs() } returns emptyList()

        val result = repository.getLocalSongs()

        assertTrue((result as Result.Success).data.isEmpty())
    }
}

