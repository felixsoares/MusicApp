package com.mobile.felix.musicapp.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("wrapperType") val wrapperType: String?,
    @SerializedName("kind") val kind: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("collectionName") val collectionName: String?,
    @SerializedName("artworkUrl60") val artworkUrl60: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    @SerializedName("previewUrl") val previewUrl: String?,
    @SerializedName("primaryGenreName") val primaryGenreName: String?
)
