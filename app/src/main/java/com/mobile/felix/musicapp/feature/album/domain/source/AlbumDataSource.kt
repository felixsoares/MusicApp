package com.mobile.felix.musicapp.feature.album.domain.source

import com.mobile.felix.musicapp.core.domain.Song

interface AlbumDataSource {
    suspend fun getAlbum(albumId: Long): List<Song>
}