package com.umbrella.nasaapiapp.model.api

import com.umbrella.nasaapiapp.model.PictureDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("apod?thumbs=true")
    suspend fun getPicture(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): PictureDTO
}