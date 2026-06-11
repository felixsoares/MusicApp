package com.mobile.felix.musicapp.feature.album.presentation

import com.mobile.felix.musicapp.core.domain.Song

data class AlbumState (
    val isLoading: Boolean = true,
    val isInternetError: Boolean = false,
    val isUnknowError: Boolean = false,
    val songs: List<Song>? = null,
)