package com.mobile.felix.musicapp.feature.song.presentation.action

sealed interface SongAction {
    data object Idle : SongAction
    data object Play : SongAction
    data object Pause : SongAction
    data object FastForward : SongAction
    data object FastRewind : SongAction
    data object Repeat : SongAction
    data class SeekTo(val position: Long) : SongAction
    data class Load(val id: Int) : SongAction
}