package com.mobile.felix.musicapp.core.presentation

import android.provider.MediaStore
import kotlinx.serialization.Serializable

sealed interface Router {
    @Serializable
    data object Home : Router

    @Serializable
    data class Detail(
        val id: Long
    ) : Router

    @Serializable
    data class Album(
        val albumId: Long,
        val album: String,
        val poster: String,
        val artist: String
    ) : Router
}