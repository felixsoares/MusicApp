package com.mobile.felix.musicapp.feature.home.data.useCase

import androidx.paging.PagingData
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetHomeSongsUseCaseTest {

    private val repository: HomeRepository = mockk()
    private lateinit var useCase: GetHomeSongsUseCase

    @Before
    fun setUp() {
        useCase = GetHomeSongsUseCase(repository)
    }

    @Test
    fun `invoke should return paged flow from repository`() {
        val expectedFlow: Flow<PagingData<Song>> = flowOf(PagingData.empty())
        every { repository.getHomeSongsPager() } returns expectedFlow

        val result = useCase.invoke()

        assertNotNull(result)
        verify(exactly = 1) { repository.getHomeSongsPager() }
    }
}

