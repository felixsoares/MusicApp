package com.mobile.felix.musicapp.feature.song.domain.repository

import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song

interface SongRepository {
    suspend fun getSong(id: Int): Result<Song>
}