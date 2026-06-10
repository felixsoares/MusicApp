package com.mobile.felix.musicapp.feature.song.data.useCase

import com.mobile.felix.musicapp.feature.song.domain.repository.SongRepository
import javax.inject.Inject

class GetSongUseCase @Inject constructor(
    private val repository: SongRepository
) {
    suspend operator fun invoke(id: Long) = repository.getSong(id)
}