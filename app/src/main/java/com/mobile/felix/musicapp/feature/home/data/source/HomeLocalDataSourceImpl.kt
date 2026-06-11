package com.mobile.felix.musicapp.feature.home.data.source

import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toDomain
import com.mobile.felix.musicapp.core.mapper.toEntity
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class HomeLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao
) : HomeLocalDataSource {

    override suspend fun markAsCachedDetail(trackId: Long) {
        songDao.markAsCachedDetails(trackId)
    }

    override suspend fun getOnlyCachedSongs(): List<Song>? {
        return songDao.getOnlyCachedSongs()?.map { it.toDomain() }
    }

    override suspend fun saveAllSongs(songs: List<Song>) {
        songDao.insertAll(songs.map { it.toEntity() })
    }

    override suspend fun clearOldHomeSearch() {
        songDao.clearOldHomeSearch()
    }
}