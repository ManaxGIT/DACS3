package com.example.video_explorer.data.youtubeData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Statistics(
    @SerialName("commentCount")
    val commentCount: String? = "0",
    @SerialName("favoriteCount")
    val favoriteCount: String,
    @SerialName("likeCount")
    val likeCount: String,
    @SerialName("viewCount")
    val viewCount: String
)