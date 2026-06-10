package com.mobile.felix.musicapp.feature.album.data.source

import com.mobile.felix.musicapp.TestFixtures.fakeSearchResponse
import com.mobile.felix.musicapp.core.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlbumDataSourceImplTest {

    private val apiService: ApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataSource: AlbumDataSourceImpl

    @Before
    fun setUp() {
        dataSource = AlbumDataSourceImpl(apiService, testDispatcher)
    }

    @Test
    fun `getAlbum returns mapped songs from api`() = runTest(testDispatcher) {
        coEvery { apiService.getAlbumTracks(albumId = 10L) } returns fakeSearchResponse

        val result = dataSource.getAlbum(10L)

        assertEquals(1, result.size)
        assertEquals("In the End", result.first().trackName)
        assertEquals("Linkin Park", result.first().artistName)
    }

    @Test
    fun `getAlbum calls api with correct albumId`() = runTest(testDispatcher) {
        coEvery { apiService.getAlbumTracks(albumId = 10L) } returns fakeSearchResponse

        dataSource.getAlbum(10L)

        coVerify { apiService.getAlbumTracks(albumId = 10L) }
    }

    @Test
    fun `getAlbum returns empty list when api returns no results`() = runTest(testDispatcher) {
        coEvery { apiService.getAlbumTracks(albumId = any()) } returns fakeSearchResponse.copy(
            resultCount = 0,
            detailResponses = emptyList()
        )

        val result = dataSource.getAlbum(10L)

        assertEquals(emptyList<Nothing>(), result)
    }
}

