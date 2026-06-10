package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetLocalSongsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend fun invoke() = homeRepository.getLocalSongs()
}