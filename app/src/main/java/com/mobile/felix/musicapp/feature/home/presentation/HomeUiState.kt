package com.mobile.felix.musicapp.feature.home.presentation

import com.mobile.felix.musicapp.core.domain.Song

data class HomeUiState(
    val songs: List<Song>? = null,
    val isLoading: Boolean = false,
    val isInternetError: Boolean = false,
    val isUnknowError: Boolean = false,
    val query: String = ""
)