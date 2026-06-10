package com.mobile.felix.musicapp.core.presentation

import kotlinx.serialization.Serializable

sealed interface Router {
    @Serializable
    data object Home : Router

    @Serializable
    data class Detail(
        val id: Int
    ) : Router

    @Serializable
    data class Album(
        val albumId: Long
    ) : Router
}