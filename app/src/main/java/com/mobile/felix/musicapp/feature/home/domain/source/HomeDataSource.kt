package com.mobile.felix.musicapp.feature.home.domain.source

import com.mobile.felix.musicapp.core.domain.Song

interface HomeDataSource {
    suspend fun getSongsByTerm(query: String): List<Song>
}