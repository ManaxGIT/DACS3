package com.example.video_explorer.data

import com.example.video_explorer.data.youtubeData.YoutubeChannel
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.network.YoutubeApiService


interface YoutubeVideoRepository {
    suspend fun getVideoDetails(videoId: String): YoutubeVideo
    suspend fun getChannelDetails(channelId: String): YoutubeChannel
}

class NetworkYoutubeVideoRepository(
    val youtubeApiService: YoutubeApiService
): YoutubeVideoRepository {
    override suspend fun getVideoDetails(videoId: String): YoutubeVideo {
        return youtubeApiService.getVideoDetails(videoId = videoId)
    }

    override suspend fun getChannelDetails(channelId: String): YoutubeChannel {
        return youtubeApiService.getChannelDetails(channelId = channelId)
    }
}
