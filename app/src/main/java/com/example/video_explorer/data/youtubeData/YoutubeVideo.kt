package com.example.video_explorer.data.youtubeData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeVideo(
    @SerialName("etag")
    val etag: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("items")
    val items: List<VideoItem>,
    @SerialName("pageInfo")
    val pageInfo: PageInfo


)