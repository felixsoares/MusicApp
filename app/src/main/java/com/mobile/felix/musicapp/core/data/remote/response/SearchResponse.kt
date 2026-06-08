package com.mobile.felix.musicapp.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val detailResponses: List<DetailResponse>
)
