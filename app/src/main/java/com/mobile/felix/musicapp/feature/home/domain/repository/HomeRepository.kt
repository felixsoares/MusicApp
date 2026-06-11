package com.mobile.felix.musicapp.feature.home.domain.repository

import androidx.paging.PagingData
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getSongsByTerm(query: String): Result<Boolean>
    suspend fun saveSongToDetailsCache(trackId: Long)
    fun getHomeSongsPager(): Flow<PagingData<Song>>
    suspend fun getLocalSongs(): Result<List<Song>>
    suspend fun clearLocalSongs()
}