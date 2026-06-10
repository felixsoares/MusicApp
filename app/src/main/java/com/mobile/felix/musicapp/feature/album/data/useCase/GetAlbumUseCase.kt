package com.mobile.felix.musicapp.feature.album.data.useCase

import com.mobile.felix.musicapp.feature.album.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend fun invoke(albumId: Long) = repository.getAlbum(albumId)
}