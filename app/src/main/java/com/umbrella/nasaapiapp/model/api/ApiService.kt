package com.umbrella.nasaapiapp.model.api

import com.umbrella.nasaapiapp.model.MeteorsDTO
import com.umbrella.nasaapiapp.model.PhotosFromMarsDTO
import com.umbrella.nasaapiapp.model.PictureDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("planetary/apod?thumbs=true")
    suspend fun getPicture(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): PictureDTO

    @GET("mars-photos/api/v1/rovers/curiosity/photos?sol=1000&page=1")
    suspend fun getPhotosFromMars(
        @Query("api_key") apiKey: String
    ): PhotosFromMarsDTO

    @GET("https://api.nasa.gov/neo/rest/v1/neo/browse")
    suspend fun getMeteorsNearEarth(
        @Query("api_key") apiKey: String
    ): MeteorsDTO
}