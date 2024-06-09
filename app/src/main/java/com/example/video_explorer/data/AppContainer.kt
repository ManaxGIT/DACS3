package com.example.video_explorer.data

import com.example.video_explorer.network.YoutubeApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val youtubeVideoRepository: YoutubeVideoRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://www.googleapis.com/youtube/v3/"

//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//        .baseUrl(baseUrl)
//        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: YoutubeApiService by lazy {
        retrofit.create(YoutubeApiService::class.java)
    }

    override val youtubeVideoRepository: YoutubeVideoRepository by lazy {
        NetworkYoutubeVideoRepository(retrofitService)
    }
}