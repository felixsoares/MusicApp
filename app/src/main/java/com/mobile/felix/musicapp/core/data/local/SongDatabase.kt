package com.mobile.felix.musicapp.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.felix.musicapp.core.data.local.dao.SongDao
import com.mobile.felix.musicapp.core.data.local.entity.SongEntity

@Database(
    entities = [
        SongEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}