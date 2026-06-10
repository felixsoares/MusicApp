package com.mobile.felix.musicapp.feature.home.domain.source

import com.mobile.felix.musicapp.core.domain.Song

interface HomeRemoteDataSource {
    suspend fun getSongsByTerm(query: String): List<Song>
}