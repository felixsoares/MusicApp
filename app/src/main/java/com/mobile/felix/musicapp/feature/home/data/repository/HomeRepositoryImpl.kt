package com.mobile.felix.musicapp.feature.home.data.repository

import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.musicapp.feature.home.domain.source.HomeDataSource
import java.io.IOException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    val dataSource: HomeDataSource
) : HomeRepository {

    override suspend fun getSongsByTerm(query: String): Result<List<Song>> {
        try {
            val result = dataSource.getSongsByTerm(query)
            return Result.Success(result)
        } catch (_: IOException) {
            return Result.Error(Failure.NetworkError)
        } catch (_: Exception) {
            return Result.Error(Failure.Unknown)
        }
    }
}