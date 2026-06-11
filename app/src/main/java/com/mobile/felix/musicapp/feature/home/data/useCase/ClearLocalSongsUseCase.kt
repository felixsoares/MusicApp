package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class ClearLocalSongsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend fun invoke() = repository.clearLocalSongs()
}