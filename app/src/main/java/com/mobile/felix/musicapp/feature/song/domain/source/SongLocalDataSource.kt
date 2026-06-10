package com.mobile.felix.musicapp.feature.song.domain.source

import com.mobile.felix.musicapp.core.domain.Song

interface SongLocalDataSource {
    suspend fun getSong(id: Int) : Song?
}