package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class SaveSongUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend fun invoke(song: Song) = repository.saveSong(song)
}