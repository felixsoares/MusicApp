package com.mobile.felix.musicapp.feature.home.domain.source

import com.mobile.felix.musicapp.core.domain.Song

interface HomeLocalDataSource {
    suspend fun markAsCachedDetail(trackId: Long)
    suspend fun getOnlyCachedSongs(): List<Song>?
    suspend fun saveAllSongs(songs: List<Song>)
    suspend fun clearOldHomeSearch()
}