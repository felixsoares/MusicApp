package com.mobile.felix.musicapp.feature.album.presentation

import com.mobile.felix.musicapp.core.domain.Song

sealed interface AlbumState {
    data object Loading : AlbumState
    data object InternetError : AlbumState
    data object UnknowError : AlbumState
    data class Data(val songs: List<Song>) : AlbumState
}