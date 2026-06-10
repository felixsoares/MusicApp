package com.mobile.felix.musicapp.feature.album.presentation

import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.album.data.useCase.GetAlbumUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getAlbumUseCase: GetAlbumUseCase = mockk()
    private lateinit var viewModel: AlbumViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumViewModel(getAlbumUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        assertEquals(AlbumState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `getAlbumById sets Data state on success`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Success(fakeSongList)

        viewModel.getAlbumById(10L)
        advanceUntilIdle()

        assertEquals(AlbumState.Data(fakeSongList), viewModel.uiState.value)
    }

    @Test
    fun `getAlbumById sets InternetError on NetworkError`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Error(Failure.NetworkError)

        viewModel.getAlbumById(10L)
        advanceUntilIdle()

        assertEquals(AlbumState.InternetError, viewModel.uiState.value)
    }

    @Test
    fun `getAlbumById sets UnknowError on Unknown failure`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Error(Failure.Unknown)

        viewModel.getAlbumById(10L)
        advanceUntilIdle()

        assertEquals(AlbumState.UnknowError, viewModel.uiState.value)
    }

    @Test
    fun `getAlbumById sets Loading state before fetching`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Success(fakeSongList)

        viewModel.getAlbumById(10L)

        advanceUntilIdle()
        assertEquals(AlbumState.Data(fakeSongList), viewModel.uiState.value)
    }

    @Test
    fun `getAlbumById with empty list sets Data with empty list`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Success(emptyList())

        viewModel.getAlbumById(10L)
        advanceUntilIdle()

        assertEquals(AlbumState.Data(emptyList()), viewModel.uiState.value)
    }
}

