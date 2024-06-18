package com.example.video_explorer.data.youtubeData


import com.example.video_explorer.data.youtubeData.parts.PageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeVideo(
    @SerialName("etag")
    val etag: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("items")
    var items: List<VideoItem>,
    @SerialName("nextPageToken")
    val nextPageToken: String,
    @SerialName("prevPageToken")
    val prevPageToken: String? = null,
    @SerialName("regionCode")
    val regionCode: String,
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)