package com.mobile.felix.musicapp.feature.home.data.repository

import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import com.mobile.felix.musicapp.feature.home.domain.source.HomeRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val localDataSource: HomeLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {

    override suspend fun getSongsByTerm(query: String): Result<List<Song>> =
        withContext(dispatcher) {
            return@withContext try {
                val result = remoteDataSource.getSongsByTerm(query)
                Result.Success(result)
            } catch (_: IOException) {
                Result.Error(Failure.NetworkError)
            } catch (_: Exception) {
                Result.Error(Failure.Unknown)
            }
        }

    override suspend fun saveSong(song: Song) = withContext(dispatcher) {
        localDataSource.saveSong(song)
    }
}