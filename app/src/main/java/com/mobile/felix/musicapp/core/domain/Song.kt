package com.mobile.felix.musicapp.core.domain


data class Song(
    val wrapperType: String?,
    val kind: String?,
    val artistName: String?,
    val trackName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val primaryGenreName: String?
)
