package com.mobile.felix.musicapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM song WHERE isFromHomeSearch = 1")
    suspend fun getHomeSongs(): List<SongEntity>

    @Query("DELETE FROM song WHERE isFromHomeSearch = 1 AND isCachedDetails = 0")
    suspend fun clearOldHomeSearch()

    @Query("UPDATE song SET isFromHomeSearch = 1 WHERE trackId = :trackId")
    suspend fun markAsHomeResult(trackId: Long)

    @Query("UPDATE song SET isCachedDetails = 1 WHERE trackId = :trackId")
    suspend fun markAsCachedDetails(trackId: Long)

    @Query("SELECT * FROM song WHERE isCachedDetails = 1")
    fun getOnlyCachedSongs(): List<SongEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<SongEntity>)

    @Query("SELECT * FROM song WHERE trackId = :trackId")
    suspend fun getSongById(trackId: Long): SongEntity?

}