package com.mobile.felix.musicapp.feature.home.presentation

import com.mobile.felix.musicapp.core.domain.Song

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object InternetError : HomeUiState
    data object UnknowError : HomeUiState
    class Data(val songs: List<Song>) : HomeUiState
}