package com.example.video_explorer.network

import com.example.video_explorer.data.youtubeData.VideoAddtitionalDetails
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.video_explorer.data.youtubeData.YoutubeChannel
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import com.example.video_explorer.data.youtubeData.parts.VideoStatisticsResponse
import retrofit2.http.POST


interface YoutubeApiService {
    @GET("search")
    suspend fun getSearchVideo(
        @Query("part") part: String = "snippet",
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("maxResults") maxResults: String = "20",
        @Query("q") query: String,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("publishedAfter") publishedAfter: String,
        @Query("videoDuration") videoDuration: String


    ): YoutubeVideo

    @GET("channels")
    suspend fun getChannelDetails(
        @Query("id") channelId: String,
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("part") part: String = "snippet,statistics,topicDetails"
    ): YoutubeChannel

    @GET("videos")
    suspend fun getVideoStatistics(
        @Query("id") videoId: String,
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("part") part: String = "statistics,contentDetails"
    ): VideoStatisticsResponse

    @GET("commentThreads")
    suspend fun getVideoCommentList(
        @Query("videoId") videoId: String,
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("part") part: String = "snippet",
        @Query("maxResult") maxResults: String = "20"
    ): YoutubeVideoComment

    @GET("videos")
    suspend fun getAdditionalDetails(
        @Query("id") videoId: String,
        @Query("key") apiKey: String = "AIzaSyDhnqxvf2uI6GOAkn7A-CEWcyErH2YSdfI",
        @Query("part") part: String = "snippet"
    ): VideoAddtitionalDetails

    @POST("rate")
    suspend fun rateVideo(
        @Query("id") videoId: String,
        @Query("rating") rating: String
    )

}