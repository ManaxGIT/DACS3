package com.example.video_explorer.data.youtubeData


import com.example.video_explorer.data.youtubeData.parts.VideoSnippet
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
    val searchResponseId: SearchResponseId,
    @SerialName("snippet")
    val videoSnippet: VideoSnippet,
    @SerialName("statistics")
    var statistics: VideoStatistics? = null,
    @SerialName("channel")
    var channel: ChannelItem? = null,

    var duration: String? = null

)

@Serializable
data class SearchResponseId(
    @SerialName("kind")
    val kind: String,
    @SerialName("videoId")
    val id: String
)