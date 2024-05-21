package com.example.video_explorer.network

import androidx.compose.ui.res.stringResource
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.video_explorer.R


interface YoutubeApiService {
    @GET("videos")
    suspend fun getVideoDetails(
        @Query("id") videoId: String,
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("part") part: String = "snippet,statistics"
    ): YoutubeVideo

    @GET("channels")
    suspend fun getChannelDetails(
        @Query("id") channelId: String,
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("part") part: String = "snippet,statistics,topicDetails"
    )
}