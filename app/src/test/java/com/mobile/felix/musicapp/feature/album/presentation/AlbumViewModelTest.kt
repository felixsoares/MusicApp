package com.mobile.felix.musicapp.feature.album.presentation

import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.album.data.useCase.GetAlbumUseCase
import com.mobile.felix.musicapp.feature.album.presentation.action.AlbumAction
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
        assertEquals(AlbumState(isLoading = true), viewModel.uiState.value)
    }

    @Test
    fun `SearchAlbum action sets Data state on success`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Success(fakeSongList)

        viewModel.submitAction(AlbumAction.SearchAlbum(10L))
        advanceUntilIdle()

        assertEquals(AlbumState(isLoading = false, songs = fakeSongList), viewModel.uiState.value)
    }

    @Test
    fun `SearchAlbum action sets InternetError on NetworkError`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Error(Failure.NetworkError)

        viewModel.submitAction(AlbumAction.SearchAlbum(10L))
        advanceUntilIdle()

        assertEquals(AlbumState(isLoading = false, isInternetError = true), viewModel.uiState.value)
    }

    @Test
    fun `SearchAlbum action sets UnknowError on Unknown failure`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Error(Failure.Unknown)

        viewModel.submitAction(AlbumAction.SearchAlbum(10L))
        advanceUntilIdle()

        assertEquals(AlbumState(isLoading = false, isUnknowError = true), viewModel.uiState.value)
    }

    @Test
    fun `SearchAlbum action sets isLoading true before fetching`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Success(fakeSongList)

        viewModel.submitAction(AlbumAction.SearchAlbum(10L))

        advanceUntilIdle()
        assertEquals(AlbumState(isLoading = false, songs = fakeSongList), viewModel.uiState.value)
    }

    @Test
    fun `SearchAlbum action with empty list sets Data with empty songs`() = runTest {
        coEvery { getAlbumUseCase.invoke(10L) } returns Result.Success(emptyList())

        viewModel.submitAction(AlbumAction.SearchAlbum(10L))
        advanceUntilIdle()

        assertEquals(AlbumState(isLoading = false, songs = emptyList()), viewModel.uiState.value)
    }
}
