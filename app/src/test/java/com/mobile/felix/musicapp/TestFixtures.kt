package com.mobile.felix.musicapp

import com.mobile.felix.musicapp.core.data.local.entity.SongEntity
import com.mobile.felix.musicapp.core.data.remote.response.DetailResponse
import com.mobile.felix.musicapp.core.data.remote.response.SearchResponse
import com.mobile.felix.musicapp.core.domain.Song

object TestFixtures {

    val fakeSong = Song(
        trackId = 1L,
        collectionId = 10L,
        artistId = 100L,
        wrapperType = "track",
        kind = "song",
        artistName = "Linkin Park",
        trackName = "In the End",
        collectionName = "Hybrid Theory",
        largePoster = "https://example.com/large.jpg",
        smallPoster = "https://example.com/small.jpg",
        songPreview = "https://example.com/preview.m4a",
        primaryGenreName = "Rock",
        durationTime = 216294L
    )

    val fakeSong2 = Song(
        trackId = 2L,
        collectionId = 10L,
        artistId = 100L,
        wrapperType = "track",
        kind = "song",
        artistName = "Linkin Park",
        trackName = "Numb",
        collectionName = "Hybrid Theory",
        largePoster = "https://example.com/large2.jpg",
        smallPoster = "https://example.com/small2.jpg",
        songPreview = "https://example.com/preview2.m4a",
        primaryGenreName = "Rock",
        durationTime = 187000L
    )

    val fakeSongList = listOf(fakeSong)

    val fakeAlbumSong = fakeSong.copy(
        trackId = null,
        wrapperType = "collection",
        kind = null,
        trackName = null,
        songPreview = null
    )
    val fakeAlbumResponseList = listOf(fakeAlbumSong, fakeSong, fakeSong2)
    val fakeAlbumSongsAfterDrop = listOf(fakeSong, fakeSong2)

    val fakeSongEntity = SongEntity(
        trackId = 1L,
        collectionId = 10L,
        artistId = 100L,
        wrapperType = "track",
        kind = "song",
        artistName = "Linkin Park",
        trackName = "In the End",
        collectionName = "Hybrid Theory",
        largePoster = "https://example.com/large.jpg",
        smallPoster = "https://example.com/small.jpg",
        songPreview = "https://example.com/preview.m4a",
        primaryGenreName = "Rock",
        durationTime = 216294L
    )

    val fakeDetailResponse = DetailResponse(
        wrapperType = "track",
        kind = "song",
        artistId = 100L,
        collectionId = 10L,
        trackId = 1L,
        artistName = "Linkin Park",
        trackName = "In the End",
        collectionName = "Hybrid Theory",
        artworkUrl60 = "https://example.com/small.jpg",
        artworkUrl100 = "https://example.com/large.jpg",
        previewUrl = "https://example.com/preview.m4a",
        primaryGenreName = "Rock",
        trackTimeMillis = 216294L
    )

    val fakeSearchResponse = SearchResponse(
        resultCount = 1,
        detailResponses = listOf(fakeDetailResponse)
    )
}

