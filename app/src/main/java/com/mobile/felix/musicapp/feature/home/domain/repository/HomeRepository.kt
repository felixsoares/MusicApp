package com.mobile.felix.musicapp.feature.home.domain.repository

import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song

interface HomeRepository {
    suspend fun getSongsByTerm(query: String): Result<List<Song>>
    suspend fun saveSong(song: Song)
    suspend fun getLocalSongs(): Result<List<Song>>
}