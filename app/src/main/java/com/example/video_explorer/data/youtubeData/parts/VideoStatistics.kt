package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoStatistics(
    @SerialName("commentCount")
    val commentCount: String? = "0",
    @SerialName("favoriteCount")
    val favoriteCount: String,
    @SerialName("likeCount")
    val likeCount: String,
    @SerialName("viewCount")
    val viewCount: String
)