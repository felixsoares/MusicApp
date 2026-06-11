package com.mobile.felix.musicapp.feature.home.data.source

import androidx.paging.PagingSource
import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toDomain
import com.mobile.felix.musicapp.core.mapper.toEntity
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import jakarta.inject.Inject

class HomeLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao
) : HomeLocalDataSource {

    override fun getSearchResultsPaged(): PagingSource<Int, SongEntity> =
        songDao.getSearchResultsPaged()

    override suspend fun getSavedSongs(): List<Song>? =
        songDao.getSavedSongs()?.map { it.toDomain() }

    override suspend fun markAsSaved(trackId: Long) =
        songDao.markAsSaved(trackId)

    override suspend fun replaceSearchResults(songs: List<Song>) =
        songDao.replaceSearchResults(songs.map { it.toEntity() })

    override suspend fun clearSearch() =
        songDao.clearSearch()
}