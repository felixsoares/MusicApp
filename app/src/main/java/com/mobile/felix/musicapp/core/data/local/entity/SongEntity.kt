package com.mobile.felix.musicapp.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "song"
)
data class SongEntity(
    @PrimaryKey(autoGenerate = false)
    val trackId: Long,
    val collectionId: Long,
    val artistId: Long,
    val wrapperType: String,
    val kind: String,
    val artistName: String,
    val trackName: String,
    val collectionName: String,
    val largePoster: String,
    val smallPoster: String,
    val songPreview: String,
    val primaryGenreName: String,
    val durationTime: Long,
    val isFromHomeSearch: Boolean = false,
    val isCachedDetails: Boolean = false
)
