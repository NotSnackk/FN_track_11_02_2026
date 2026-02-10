package com.example.fortnite_tracker.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.example.fortnite_tracker.models.BrStatsResponse

interface FortniteApiService {

    @GET("v2/stats/br/v2")
    suspend fun getBrStats(
        @Query("name") nickname: String,
        @Query("accountType") accountType: String? = null,
        @Query("timeWindow") timeWindow: String? = null,
        @Query("image") image: String? = null
    ): Response<BrStatsResponse>
}
