package com.mobile.felix.musicapp.core.domain


data class Song(
    val trackId: Long?,
    val collectionId: Long?,
    val artistId: Long?,
    val wrapperType: String?,
    val kind: String?,
    val artistName: String?,
    val trackName: String?,
    val collectionName: String?,
    val largePoster: String?,
    val smallPoster: String?,
    val songPreview: String?,
    val primaryGenreName: String?,
    val durationTime: Long?
)
