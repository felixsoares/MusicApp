package com.mobile.felix.musicapp.feature.album.data.repository

import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.album.domain.repository.AlbumRepository
import com.mobile.felix.musicapp.feature.album.domain.source.AlbumDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val dataSource: AlbumDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AlbumRepository {

    override suspend fun getAlbum(albumId: Long): Result<List<Song>> = withContext(dispatcher) {
        withContext(dispatcher) {
            return@withContext try {
                val result = dataSource.getAlbum(albumId)
                Result.Success(result)
            } catch (_: IOException) {
                Result.Error(Failure.NetworkError)
            } catch (_: Exception) {
                Result.Error(Failure.Unknown)
            }
        }
    }
}
