package com.mobile.felix.musicapp.feature.home.data.source

import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.TestFixtures.fakeSongEntity
import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.mapper.toEntity
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
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
    fun `saveSong inserts entity into dao`() = runTest {
        val entity = fakeSong.toEntity()
        coJustRun { songDao.insert(entity) }

        dataSource.saveSong(fakeSong)

        coVerify(exactly = 1) { songDao.insert(entity) }
    }

    @Test
    fun `getSongs returns mapped songs from dao`() = runTest {
        every { songDao.getSongs() } returns listOf(fakeSongEntity)

        val result = dataSource.getSongs()

        assertEquals(1, result?.size)
        assertEquals("In the End", result?.first()?.trackName)
    }

    @Test
    fun `getSongs returns null when dao returns null`() = runTest {
        every { songDao.getSongs() } returns null

        val result = dataSource.getSongs()

        assertNull(result)
    }

    @Test
    fun `getSongs returns empty list when dao returns empty list`() = runTest {
        every { songDao.getSongs() } returns emptyList()

        val result = dataSource.getSongs()

        assertEquals(emptyList<Nothing>(), result)
    }
}

