package com.example.video_explorer.data.youtubeData


import com.example.video_explorer.data.youtubeData.parts.PageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeVideoComment(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("nextPageToken")
    val nextPageToken: String? = null,
    @SerialName("pageInfo")
    val pageInfo: PageInfo,
    @SerialName("items")
    val items: List<CommentItem>
)