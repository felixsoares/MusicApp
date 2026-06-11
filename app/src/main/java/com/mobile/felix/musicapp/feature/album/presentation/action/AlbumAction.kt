package com.mobile.felix.musicapp.feature.album.presentation.action

import com.mobile.felix.musicapp.core.domain.Song

sealed interface AlbumAction {
    data class SearchAlbum(val albumId: Long) : AlbumAction
}