package com.mobile.felix.musicapp.feature.song.presentation

import com.mobile.felix.musicapp.TestFixtures.fakeSong
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.song.data.useCase.GetSongUseCase
import com.mobile.felix.musicapp.feature.song.domain.player.AudioPlayer
import com.mobile.felix.musicapp.feature.song.presentation.action.SongAction
import io.mockk.every
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class SongViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val audioPlayer: AudioPlayer = mockk(relaxed = true)
    private val useCase: GetSongUseCase = mockk()

    private lateinit var viewModel: SongViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { audioPlayer.currentPositionFlow } returns MutableStateFlow(0L)
        viewModel = SongViewModel(audioPlayer, useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has correct defaults`() {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.hasError)
        assertEquals(null, state.song)
        assertEquals(PlaybackState.Idle, state.playbackState)
    }

    @Test
    fun `Load action with valid song starts playback`() = runTest {
        coEvery { useCase.invoke(1L) } returns Result.Success(fakeSong)

        viewModel.submitAction(SongAction.Load(1L))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.hasError)
        assertEquals(fakeSong, state.song)
        assertEquals(PlaybackState.Playing, state.playbackState)
        verify { audioPlayer.play(fakeSong.songPreview!!) }
    }

    @Test
    fun `Load action with null songPreview sets Error playback state`() = runTest {
        val songWithNoPreview = fakeSong.copy(songPreview = null)
        coEvery { useCase.invoke(1L) } returns Result.Success(songWithNoPreview)

        viewModel.submitAction(SongAction.Load(1L))
        advanceUntilIdle()

        assertEquals(PlaybackState.Error, viewModel.uiState.value.playbackState)
    }

    @Test
    fun `Load action on error sets hasError`() = runTest {
        coEvery { useCase.invoke(1L) } returns Result.Error(Failure.Unknown)

        viewModel.submitAction(SongAction.Load(1L))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.hasError)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `Play action resumes player and sets Playing state`() = runTest {
        viewModel.submitAction(SongAction.Play)
        advanceUntilIdle()

        assertEquals(PlaybackState.Playing, viewModel.uiState.value.playbackState)
        verify { audioPlayer.resume() }
    }

    @Test
    fun `Pause action pauses player and sets Paused state`() = runTest {
        viewModel.submitAction(SongAction.Pause)
        advanceUntilIdle()

        assertEquals(PlaybackState.Paused, viewModel.uiState.value.playbackState)
        verify { audioPlayer.pause() }
    }

    @Test
    fun `FastForward action calls fastForward and sets Playing state`() = runTest {
        viewModel.submitAction(SongAction.FastForward)
        advanceUntilIdle()

        assertEquals(PlaybackState.Playing, viewModel.uiState.value.playbackState)
        verify { audioPlayer.fastForward() }
    }

    @Test
    fun `FastRewind action calls fastRewind and sets Playing state`() = runTest {
        viewModel.submitAction(SongAction.FastRewind)
        advanceUntilIdle()

        assertEquals(PlaybackState.Playing, viewModel.uiState.value.playbackState)
        verify { audioPlayer.fastRewind() }
    }

    @Test
    fun `Repeat action calls repeat and sets Playing state`() = runTest {
        viewModel.submitAction(SongAction.Repeat)
        advanceUntilIdle()

        assertEquals(PlaybackState.Playing, viewModel.uiState.value.playbackState)
        verify { audioPlayer.repeat() }
    }

    @Test
    fun `SeekTo action calls seekTo with correct position`() = runTest {
        viewModel.submitAction(SongAction.SeekTo(5000L))
        advanceUntilIdle()

        assertEquals(PlaybackState.Playing, viewModel.uiState.value.playbackState)
        verify { audioPlayer.seekTo(5000L) }
    }

    @Test
    fun `onCleared pauses and releases player`() {
        viewModel.onCleared()

        verify { audioPlayer.pause() }
        verify { audioPlayer.release() }
    }
}

