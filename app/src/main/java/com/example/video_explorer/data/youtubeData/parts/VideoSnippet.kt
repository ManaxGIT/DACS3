package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoSnippet(
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("channelId")
    val channelId: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    var description: String,
    @SerialName("thumbnails")
    val thumbnails: Thumbnails,
    @SerialName("channelTitle")
    val channelTitle: String,
    @SerialName("liveBroadcastContent")
    val liveBroadcastContent: String,
    @SerialName("publishTime")
    val publishTime: String,

    var tags: List<String>? = null,
    var descriptionFlag: Boolean = true
)

@Serializable
data class Localized(
    @SerialName("description")
    val description: String,
    @SerialName("title")
    val title: String
)