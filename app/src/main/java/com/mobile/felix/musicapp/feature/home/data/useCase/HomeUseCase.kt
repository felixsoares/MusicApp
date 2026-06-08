package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    suspend fun getSongsByTerm(query: String) = repository.getSongsByTerm(query)
}