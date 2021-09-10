package com.umbrella.nasaapiapp.model.api

import com.umbrella.nasaapiapp.model.PictureDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("apod")
    suspend fun getPicture(
        @Query("api_key") apiKey: String
    ): PictureDTO
}