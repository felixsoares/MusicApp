package com.mobile.felix.musicapp.feature.home.data.source

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

class HomeRemoteDataSourceImplTest {

    private val apiService: ApiService = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataSource: HomeRemoteDataSourceImpl

    @Before
    fun setUp() {
        dataSource = HomeRemoteDataSourceImpl(apiService, testDispatcher)
    }

    @Test
    fun `getSongsByTerm returns mapped songs from api`() = runTest(testDispatcher) {
        coEvery { apiService.searchContent(term = "rock") } returns fakeSearchResponse

        val result = dataSource.getSongsByTerm("rock")

        assertEquals(1, result.size)
        assertEquals("In the End", result.first().trackName)
        assertEquals("Linkin Park", result.first().artistName)
    }

    @Test
    fun `getSongsByTerm calls api with correct query`() = runTest(testDispatcher) {
        coEvery { apiService.searchContent(term = "linkin park") } returns fakeSearchResponse

        dataSource.getSongsByTerm("linkin park")

        coVerify { apiService.searchContent(term = "linkin park") }
    }

    @Test
    fun `getSongsByTerm returns empty list when api returns no results`() = runTest(testDispatcher) {
        coEvery { apiService.searchContent(term = any()) } returns fakeSearchResponse.copy(
            resultCount = 0,
            detailResponses = emptyList()
        )

        val result = dataSource.getSongsByTerm("unknown")

        assertEquals(emptyList<Nothing>(), result)
    }
}

