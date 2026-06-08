package com.mobile.felix.musicapp.feature.home.data.source

import com.mobile.felix.musicapp.core.data.remote.ApiService
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toResultList
import com.mobile.felix.musicapp.feature.home.domain.source.HomeDataSource
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    val service: ApiService
) : HomeDataSource {
    override suspend fun getSongsByTerm(query: String): List<Song> {
        val response = service.searchContent(term = query)
        val resultList = response.toResultList()
        return resultList
    }
}