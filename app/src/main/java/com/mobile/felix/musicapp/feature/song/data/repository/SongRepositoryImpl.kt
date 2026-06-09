package com.mobile.felix.musicapp.feature.song.data.repository

import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.song.domain.repository.SongRepository
import com.mobile.felix.musicapp.feature.song.domain.source.SongDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val songDataSource: SongDataSource,
    private val dispatcher: CoroutineDispatcher
) : SongRepository {
    override suspend fun getSong(id: Int): Result<Song> = withContext(dispatcher) {
        return@withContext try {
            val song = songDataSource.getSong(id)
            if (song != null) {
                Result.Success(song)
            } else {
                Result.Error(Failure.Unknown)
            }
        } catch (e: Exception) {
            Result.Error(Failure.Unknown)
        }
    }

}