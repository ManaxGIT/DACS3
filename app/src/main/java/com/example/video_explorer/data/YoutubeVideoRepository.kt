package com.example.video_explorer.data

import com.example.video_explorer.data.youtubeData.VideoAddtitionalDetails
import com.example.video_explorer.data.youtubeData.YoutubeChannel
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import com.example.video_explorer.data.youtubeData.parts.VideoStatisticsResponse
import com.example.video_explorer.network.YoutubeApiService


interface YoutubeVideoRepository {
    suspend fun getAdditionalDetails(videoId: String): VideoAddtitionalDetails
    suspend fun getVideoStatistics(videoId: String): VideoStatisticsResponse
    suspend fun getChannelDetails(channelId: String): YoutubeChannel
    suspend fun getVideoCommentList(videoId: String): YoutubeVideoComment
    suspend fun getSearchVideo(query: String): YoutubeVideo
}

class NetworkYoutubeVideoRepository(
    val youtubeApiService: YoutubeApiService
): YoutubeVideoRepository {
    override suspend fun getAdditionalDetails(videoId: String): VideoAddtitionalDetails {
        return youtubeApiService.getAdditionalDetails(videoId = videoId)
    }

    override suspend fun getVideoStatistics(videoId: String): VideoStatisticsResponse {
        return youtubeApiService.getVideoStatistics(videoId = videoId)
    }

    override suspend fun getChannelDetails(channelId: String): YoutubeChannel {
        return youtubeApiService.getChannelDetails(channelId = channelId)
    }

    override suspend fun getVideoCommentList(videoId: String): YoutubeVideoComment {
        return youtubeApiService.getVideoCommentList(videoId = videoId)
    }

    override suspend fun getSearchVideo(query: String): YoutubeVideo {
        return youtubeApiService.getSearchVideo(query = query, apiKey = "AIzaSyDkNTLceNFPSBUcUgFHSOVy5_CnfmUxzyQ")
    }
}
