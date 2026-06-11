package com.mobile.felix.musicapp.core.mapper

import com.mobile.felix.musicapp.core.data.local.entity.SongEntity
import com.mobile.felix.musicapp.core.data.remote.response.SearchResponse
import com.mobile.felix.musicapp.core.domain.Song

fun SearchResponse.toResultList() = detailResponses.map {
    Song(
        trackId = it.trackId,
        collectionId = it.collectionId,
        artistId = it.artistId,
        wrapperType = it.wrapperType,
        kind = it.kind,
        artistName = it.artistName,
        trackName = it.trackName,
        collectionName = it.collectionName,
        largePoster = it.artworkUrl100,
        smallPoster = it.artworkUrl60,
        songPreview = it.previewUrl,
        primaryGenreName = it.primaryGenreName,
        durationTime = it.trackTimeMillis
    )
}

fun SongEntity.toDomain() = Song(
    trackId = trackId,
    collectionId = collectionId,
    artistId = artistId,
    wrapperType = wrapperType,
    kind = kind,
    artistName = artistName,
    trackName = trackName,
    collectionName = collectionName,
    largePoster = largePoster,
    smallPoster = smallPoster,
    songPreview = songPreview,
    primaryGenreName = primaryGenreName,
    durationTime = durationTime
)

fun Song.toEntity() = SongEntity(
    trackId = trackId ?: 0,
    collectionId = collectionId ?: 0,
    artistId = artistId ?: 0,
    wrapperType = wrapperType ?: "",
    kind = kind ?: "",
    artistName = artistName ?: "",
    trackName = trackName ?: "",
    collectionName = collectionName ?: "",
    largePoster = largePoster ?: "",
    smallPoster = smallPoster ?: "",
    songPreview = songPreview ?: "",
    primaryGenreName = primaryGenreName ?: "",
    durationTime = durationTime ?: 0L,
    isFromHomeSearch = false,
    isCachedDetails = false
)