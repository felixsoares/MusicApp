package com.mobile.felix.musicapp.feature.home.domain.source

import com.mobile.felix.musicapp.core.domain.Song

interface HomeLocalDataSource {
    suspend fun saveSong(song: Song)
    suspend fun getSongs(): List<Song>?
}