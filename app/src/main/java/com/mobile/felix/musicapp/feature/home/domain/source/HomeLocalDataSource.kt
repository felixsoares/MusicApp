package com.mobile.felix.musicapp.feature.home.domain.source

import androidx.paging.PagingSource
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity
import com.mobile.felix.musicapp.core.domain.Song

interface HomeLocalDataSource {
    fun getSearchResultsPaged(): PagingSource<Int, SongEntity>
    suspend fun getSavedSongs(): List<Song>?
    suspend fun markAsSaved(trackId: Long)
    suspend fun replaceSearchResults(songs: List<Song>)
    suspend fun clearSearch()
}