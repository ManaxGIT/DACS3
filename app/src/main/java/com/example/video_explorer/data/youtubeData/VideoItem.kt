package com.example.video_explorer.data.youtubeData


import com.example.video_explorer.data.youtubeData.parts.Snippet
import com.example.video_explorer.data.youtubeData.parts.VideoStatistics
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoItem(
    @SerialName("etag")
    val etag: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("id")
    val id: String,
    @SerialName("snippet")
    val snippet: Snippet,
    @SerialName("statistics")
    val statistics: VideoStatistics,
    @SerialName("channel")
    var channelItem: ChannelItem? = null
)