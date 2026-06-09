package com.mobile.felix.musicapp.feature.song.data.source

import com.mobile.felix.musicapp.core.domain.Song
import com.mobile.felix.musicapp.feature.song.domain.source.SongDataSource
import javax.inject.Inject

class SongDataSourceImpl @Inject constructor() : SongDataSource {
    override suspend fun getSong(id: Int): Song? {
        return Song(
            trackId = 1,
            collectionId = 1,
            artistId = 1,
            wrapperType = "track",
            kind = "song",
            artistName = "Linkin park",
            trackName = "Lying from you",
            collectionName = "Collection Name",
            largePoster = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/53/a7/7f/53a77fab-c54c-a57b-8130-248fc12d0c80/093624948995.jpg/100x100bb.jpg",
            smallPoster = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/53/a7/7f/53a77fab-c54c-a57b-8130-248fc12d0c80/093624948995.jpg/60x60bb.jpg",
            songPreview = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/3f/cb/c7/3fcbc7cc-0606-7f6e-7fc3-793318cfd1ed/mzaf_16081918663584534594.plus.aac.p.m4a",
            primaryGenreName = "Hard rock",
            durationTime = 216294,
        )
    }
}