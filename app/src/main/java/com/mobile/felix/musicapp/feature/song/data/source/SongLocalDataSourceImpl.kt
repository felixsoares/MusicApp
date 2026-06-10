package com.mobile.felix.musicapp.feature.song.data.source

import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toDomain
import com.mobile.felix.musicapp.feature.song.domain.source.SongLocalDataSource
import javax.inject.Inject

class SongLocalDataSourceImpl @Inject constructor(
    private val songDao: SongDao
) : SongLocalDataSource {
    override suspend fun getSong(id: Long): Song? {
        return songDao.getSongById(id)?.toDomain()
    }
}