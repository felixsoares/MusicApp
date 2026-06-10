package com.mobile.felix.musicapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM song")
    fun getSongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songEntity: SongEntity)

    @Query("SELECT * FROM song WHERE trackId = :trackId")
    suspend fun getSongById(trackId: Int): SongEntity?

}