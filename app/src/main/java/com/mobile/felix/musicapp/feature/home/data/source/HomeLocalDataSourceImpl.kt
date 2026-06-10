package com.mobile.felix.musicapp.feature.home.data.source

import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toEntity
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import jakarta.inject.Inject

class HomeLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao
) : HomeLocalDataSource {
    override suspend fun saveSong(song: Song) {
        val songEntity = song.toEntity()
        songDao.insert(songEntity)
    }
}