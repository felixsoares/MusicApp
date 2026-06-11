package com.mobile.felix.musicapp.feature.home.data.source

import com.mobile.felix.musicapp.TestFixtures.fakeSongEntity
import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.mapper.toEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class HomeLocalDataSourceImplTest {

    private val songDao: SongDao = mockk()
    private lateinit var dataSource: HomeLocalDataSourceImpl

    @Before
    fun setUp() {
        dataSource = HomeLocalDataSourceImpl(songDao)
    }

    @Test
    fun `replaceSearchResults delegates to songDao`() = runTest {
        coJustRun { songDao.replaceSearchResults(any()) }

        dataSource.replaceSearchResults(fakeSongList)

        coVerify(exactly = 1) { songDao.replaceSearchResults(fakeSongList.map { it.toEntity() }) }
    }

    @Test
    fun `clearSearch delegates to songDao`() = runTest {
        coJustRun { songDao.clearSearch() }

        dataSource.clearSearch()

        coVerify(exactly = 1) { songDao.clearSearch() }
    }

    @Test
    fun `markAsSaved delegates trackId to songDao`() = runTest {
        coJustRun { songDao.markAsSaved(1L) }

        dataSource.markAsSaved(1L)

        coVerify(exactly = 1) { songDao.markAsSaved(1L) }
    }

    @Test
    fun `getSavedSongs returns mapped songs from dao`() = runTest {
        coEvery { songDao.getSavedSongs() } returns listOf(fakeSongEntity)

        val result = dataSource.getSavedSongs()

        assertEquals(1, result?.size)
        assertEquals("In the End", result?.first()?.trackName)
    }

    @Test
    fun `getSavedSongs returns null when dao returns null`() = runTest {
        coEvery { songDao.getSavedSongs() } returns null

        val result = dataSource.getSavedSongs()

        assertNull(result)
    }

    @Test
    fun `getSavedSongs returns empty list when dao returns empty list`() = runTest {
        coEvery { songDao.getSavedSongs() } returns emptyList()

        val result = dataSource.getSavedSongs()

        assertEquals(emptyList<Nothing>(), result)
    }
}

