package com.example.wallpaperapplication.data.remote

import com.example.wallpaperapplication.model.UnsplashResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WallpaperApi {


    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "TFxytloHfrXLMRzpic0TdhHR-xbLIcz8KiiXNreWXVg"
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per-page") perPage: Int
    ): UnsplashResponse
}