package com.mobile.felix.musicapp.core.data.remote

import com.mobile.felix.musicapp.core.data.remote.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun searchContent(
        @Query("term") term: String,
        @Query("country") country: String = "US",
        @Query("media") media: String = "music",
        @Query("entity") entity: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("lang") lang: String = "en_us",
        @Query("explicit") explicit: String = "Yes"
    ): SearchResponse

    @GET("lookup")
    suspend fun getAlbumTracks(
        @Query("id") albumId: Long,
        @Query("entity") entity: String = "song"
    ): SearchResponse
}