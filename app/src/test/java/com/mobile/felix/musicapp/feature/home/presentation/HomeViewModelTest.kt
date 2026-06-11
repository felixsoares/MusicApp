package com.mobile.felix.musicapp.feature.home.presentation

import androidx.paging.PagingData
import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.TestFixtures.fakeSongList
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.home.data.useCase.ClearLocalSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetHomeSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetLocalSongsUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.GetSongsByTermUseCase
import com.mobile.felix.musicapp.feature.home.data.useCase.SaveSongUseCase
import com.mobile.felix.musicapp.feature.home.presentation.action.HomeAction
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getSongsByTermUseCase: GetSongsByTermUseCase = mockk()
    private val saveSongUseCase: SaveSongUseCase = mockk()
    private val getLocalSongsUseCase: GetLocalSongsUseCase = mockk()
    private val clearLocalSongsUseCase: ClearLocalSongsUseCase = mockk()
    private val getHomeSongsUseCase: GetHomeSongsUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getLocalSongsUseCase.invoke() } returns Result.Success(emptyList())
        coJustRun { clearLocalSongsUseCase.invoke() }
        every { getHomeSongsUseCase.invoke() } returns flowOf(PagingData.empty())
        viewModel = HomeViewModel(
            getSongsByTermUseCase,
            saveSongUseCase,
            getLocalSongsUseCase,
            clearLocalSongsUseCase,
            getHomeSongsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has isLoading true`() {
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GetLocalSongs action returns songs on success`() = runTest {
        coEvery { getLocalSongsUseCase.invoke() } returns Result.Success(fakeSongList)

        viewModel.submitAction(HomeAction.GetLocalSongs)
        advanceUntilIdle()

        assertEquals(fakeSongList, viewModel.uiState.value.songs)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isInternetError)
        assertFalse(viewModel.uiState.value.isUnknowError)
    }

    @Test
    fun `GetLocalSongs action sets internet error on NetworkError`() = runTest {
        coEvery { getLocalSongsUseCase.invoke() } returns Result.Error(Failure.NetworkError)

        viewModel.submitAction(HomeAction.GetLocalSongs)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isInternetError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GetLocalSongs action sets unknown error on Unknown failure`() = runTest {
        coEvery { getLocalSongsUseCase.invoke() } returns Result.Error(Failure.Unknown)

        viewModel.submitAction(HomeAction.GetLocalSongs)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isUnknowError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Search action updates query and sets isLoading false after debounce`() = runTest {
        coEvery { getSongsByTermUseCase.invoke("rock") } returns Result.Success(true)

        viewModel.submitAction(HomeAction.Search("rock"))
        advanceTimeBy(400)
        advanceUntilIdle()

        assertEquals("rock", viewModel.uiState.value.query)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isInternetError)
    }

    @Test
    fun `Search action with blank query loads local songs`() = runTest {
        coEvery { getLocalSongsUseCase.invoke() } returns Result.Success(fakeSongList)

        viewModel.submitAction(HomeAction.Search(""))
        advanceTimeBy(400)
        advanceUntilIdle()

        assertEquals(fakeSongList, viewModel.uiState.value.songs)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Search action sets internet error when fetch fails with NetworkError`() = runTest {
        coEvery { getSongsByTermUseCase.invoke("rock") } returns Result.Error(Failure.NetworkError)

        viewModel.submitAction(HomeAction.Search("rock"))
        advanceTimeBy(400)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isInternetError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Search action sets unknown error when fetch fails with Unknown failure`() = runTest {
        coEvery { getSongsByTermUseCase.invoke("rock") } returns Result.Error(Failure.Unknown)

        viewModel.submitAction(HomeAction.Search("rock"))
        advanceTimeBy(400)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isUnknowError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `SaveSong action delegates trackId to use case`() = runTest {
        val trackId = fakeSong.trackId!!
        coJustRun { saveSongUseCase.invoke(trackId) }

        viewModel.submitAction(HomeAction.SaveSong(trackId))
        advanceUntilIdle()

        coVerify(exactly = 1) { saveSongUseCase.invoke(trackId) }
    }
}
