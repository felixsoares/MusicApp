package com.mobile.felix.musicapp.feature.song.data.repository

import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.song.domain.source.SongLocalDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SongRepositoryImplTest {

    private val dataSource: SongLocalDataSource = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SongRepositoryImpl

    @Before
    fun setUp() {
        repository = SongRepositoryImpl(dataSource, testDispatcher)
    }

    @Test
    fun `getSong returns success when data source returns song`() = runTest(testDispatcher) {
        coEvery { dataSource.getSong(1L) } returns fakeSong

        val result = repository.getSong(1L)

        assertEquals(Result.Success(fakeSong), result)
    }

    @Test
    fun `getSong returns Unknown error when data source returns null`() = runTest(testDispatcher) {
        coEvery { dataSource.getSong(1L) } returns null

        val result = repository.getSong(1L)

        assertEquals(Result.Error(Failure.Unknown), result)
    }

    @Test
    fun `getSong returns Unknown error when data source throws exception`() = runTest(testDispatcher) {
        coEvery { dataSource.getSong(1L) } throws RuntimeException("DB error")

        val result = repository.getSong(1L)

        assertEquals(Result.Error(Failure.Unknown), result)
    }
}

