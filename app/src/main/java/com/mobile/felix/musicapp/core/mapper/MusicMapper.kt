package com.mobile.felix.musicapp.core.mapper

import com.mobile.felix.musicapp.core.data.remote.response.SearchResponse
import com.mobile.felix.musicapp.core.domain.Song

fun SearchResponse.toResultList() = detailResponses.map {
    Song(
        wrapperType = it.wrapperType,
        kind = it.kind,
        artistName = it.artistName,
        trackName = it.trackName,
        collectionName = it.collectionName,
        artworkUrl100 = it.artworkUrl100,
        artworkUrl60 = it.artworkUrl60,
        previewUrl = it.previewUrl,
        primaryGenreName = it.primaryGenreName
    )
}