package com.mobile.felix.musicapp.feature.home.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.core.mapper.toDomain
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import com.mobile.felix.musicapp.feature.home.domain.source.HomeLocalDataSource
import com.mobile.felix.musicapp.feature.home.domain.source.HomeRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val localDataSource: HomeLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {

    override suspend fun getSongsByTerm(query: String): Result<Boolean> =
        withContext(dispatcher) {
            return@withContext try {
                val songs = remoteDataSource.getSongsByTerm(query)
                localDataSource.replaceSearchResults(songs)
                Result.Success(true)
            } catch (ioe: IOException) {
                Log.e("HomeRepository", "${ioe.message}")
                Result.Error(Failure.NetworkError)
            } catch (e: Exception) {
                Log.e("HomeRepository", "${e.message}")
                Result.Error(Failure.Unknown)
            }
        }

    override suspend fun saveSongToDetailsCache(trackId: Long) = withContext(dispatcher) {
        localDataSource.markAsSaved(trackId)
    }

    override fun getHomeSongsPager(): Flow<PagingData<Song>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localDataSource.getSearchResultsPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getLocalSongs(): Result<List<Song>> = withContext(dispatcher) {
        val savedSongs = localDataSource.getSavedSongs()
        return@withContext Result.Success(savedSongs ?: emptyList())
    }

    override suspend fun clearLocalSongs() {
        localDataSource.clearSearch()
    }
}