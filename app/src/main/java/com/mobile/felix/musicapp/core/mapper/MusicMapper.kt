package com.mobile.felix.musicapp.core.mapper

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
        primaryGenreName = it.primaryGenreName
    )
}