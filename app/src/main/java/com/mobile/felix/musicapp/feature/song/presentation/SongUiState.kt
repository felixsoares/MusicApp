package com.mobile.felix.musicapp.feature.song.presentation

import com.mobile.felix.musicapp.core.domain.Song

data class SongUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val song: Song? = null,
    val playbackState: PlaybackState = PlaybackState.Idle,
    val playbackPosition: Long = 0L,
)

sealed interface PlaybackState {
    data object Idle : PlaybackState
    data object Playing : PlaybackState
    data object Paused : PlaybackState
    data object Error : PlaybackState
}