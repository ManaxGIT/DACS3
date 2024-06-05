package com.example.video_explorer.data.youtubeData


import com.example.video_explorer.data.youtubeData.parts.PageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeChannel(
    @SerialName("etag")
    val etag: String,
    @SerialName("items")
    val items: List<ChannelItem>,
    @SerialName("kind")
    val kind: String,
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)