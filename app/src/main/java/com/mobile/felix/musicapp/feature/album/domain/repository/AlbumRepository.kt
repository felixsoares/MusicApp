package com.mobile.felix.musicapp.feature.album.domain.repository

import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song

interface AlbumRepository {
    suspend fun getAlbum(albumId: Long): Result<List<Song>>
}