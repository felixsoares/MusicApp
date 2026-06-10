package com.mobile.felix.musicapp.feature.home.data.source

import com.mobile.felix.musicapp.core.data.remote.ApiService
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toResultList
import com.mobile.felix.musicapp.feature.home.domain.source.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val service: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeDataSource {
    override suspend fun getSongsByTerm(query: String): List<Song> = withContext(dispatcher) {
        val response = service.searchContent(term = query)
        val resultList = response.toResultList()
        return@withContext resultList
    }
}