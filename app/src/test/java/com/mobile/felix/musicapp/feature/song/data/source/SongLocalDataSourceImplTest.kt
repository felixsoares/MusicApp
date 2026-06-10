package com.mobile.felix.musicapp.feature.song.data.source

import com.mobile.felix.musicapp.TestFixtures.fakeSongEntity
import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class SongLocalDataSourceImplTest {

    private val songDao: SongDao = mockk()
    private lateinit var dataSource: SongLocalDataSourceImpl

    @Before
    fun setUp() {
        dataSource = SongLocalDataSourceImpl(songDao)
    }

    @Test
    fun `getSong returns mapped song when dao finds entity`() = runTest {
        coEvery { songDao.getSongById(1L) } returns fakeSongEntity

        val result = dataSource.getSong(1L)

        assertEquals("In the End", result?.trackName)
        assertEquals(1L, result?.trackId)
        assertEquals("Linkin Park", result?.artistName)
    }

    @Test
    fun `getSong returns null when dao returns null`() = runTest {
        coEvery { songDao.getSongById(99L) } returns null

        val result = dataSource.getSong(99L)

        assertNull(result)
    }
}

