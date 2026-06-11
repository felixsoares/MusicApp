package com.mobile.felix.musicapp.feature.home.data.useCase

import androidx.paging.PagingData
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeSongsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<PagingData<Song>> {
        return repository.getHomeSongsPager()
    }
}