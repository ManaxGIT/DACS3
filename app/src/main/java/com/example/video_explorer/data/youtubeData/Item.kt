package com.example.video_explorer.data.youtubeData


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("etag")
    val etag: String,
    @SerialName("kind")
    val kind: String,
    @SerialName("id")
    val id: String,
    @SerialName("snippet")
    val snippet: Snippet,
    @SerialName("statistics")
    val statistics: Statistics
)