package com.mobile.felix.musicapp.feature.home.presentation.action

import com.mobile.felix.musicapp.core.domain.Song

interface HomeAction {
    data object Idle : HomeAction
    data class Search(val query: String) : HomeAction
    data object GetLocalSongs : HomeAction

    data class SaveSong(val trackId: Long) : HomeAction
}