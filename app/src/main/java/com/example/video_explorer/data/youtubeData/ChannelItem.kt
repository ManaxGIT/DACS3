package com.example.video_explorer.data.youtubeData


import com.example.video_explorer.data.youtubeData.parts.ChannelSnippet
import com.example.video_explorer.data.youtubeData.parts.ChannelStatistics
import com.example.video_explorer.data.youtubeData.parts.Snippet
import com.example.video_explorer.data.youtubeData.parts.TopicDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelItem(
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("snippet")
    val snippet: ChannelSnippet,
    @SerialName("statistics")
    val statistics: ChannelStatistics,
    @SerialName("topicDetails")
    val topicDetails: TopicDetails
)