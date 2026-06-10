package com.mobile.felix.musicapp.feature.album.data.source

import com.mobile.felix.musicapp.core.data.remote.ApiService
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toResultList
import com.mobile.felix.musicapp.feature.album.domain.source.AlbumDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AlbumDataSource {
    override suspend fun getAlbum(albumId: Long): List<Song> = withContext(dispatcher) {
        val response = apiService.getAlbumTracks(albumId = albumId)
        val results = response.toResultList()
        return@withContext results
    }
}