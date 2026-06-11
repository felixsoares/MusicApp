package com.mobile.felix.musicapp.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity

@Dao
abstract class SongDao {

    @Query("SELECT * FROM song WHERE isFromHomeSearch = 1 ORDER BY trackName ASC")
    abstract fun getSearchResultsPaged(): PagingSource<Int, SongEntity>
    @Query("SELECT * FROM song WHERE isCachedDetails = 1 ORDER BY trackName ASC")
    abstract suspend fun getSavedSongs(): List<SongEntity>?
    @Query("UPDATE song SET isCachedDetails = 1 WHERE trackId = :trackId")
    abstract suspend fun markAsSaved(trackId: Long)

    @Transaction
    open suspend fun replaceSearchResults(songs: List<SongEntity>) {
        deleteUnsavedSongs()
        resetSearchFlags()
        insertIgnoring(songs)
        tagAsSearchResults(songs.map { it.trackId })
    }

    @Transaction
    open suspend fun clearSearch() {
        deleteUnsavedSongs()
        resetSearchFlags()
    }

    @Query("SELECT * FROM song WHERE trackId = :trackId")
    abstract suspend fun getSongById(trackId: Long): SongEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertIgnoring(songs: List<SongEntity>)

    @Query("DELETE FROM song WHERE isCachedDetails = 0")
    protected abstract suspend fun deleteUnsavedSongs()

    @Query("UPDATE song SET isFromHomeSearch = 0")
    protected abstract suspend fun resetSearchFlags()

    @Query("UPDATE song SET isFromHomeSearch = 1 WHERE trackId IN (:trackIds)")
    protected abstract suspend fun tagAsSearchResults(trackIds: List<Long>)
}