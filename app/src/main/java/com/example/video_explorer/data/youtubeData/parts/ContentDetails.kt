package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentDetails(
    @SerialName("caption")
    val caption: String,
    @SerialName("contentRating")
    val contentRating: ContentRating,
    @SerialName("definition")
    val definition: String,
    @SerialName("dimension")
    val dimension: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("licensedContent")
    val licensedContent: Boolean,
    @SerialName("projection")
    val projection: String
)