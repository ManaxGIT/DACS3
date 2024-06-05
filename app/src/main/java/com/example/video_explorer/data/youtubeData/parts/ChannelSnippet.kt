package com.example.video_explorer.data.youtubeData.parts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelSnippet(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("customUrl")
    val customUrl: String,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("thumbnails")
    val thumbnails: Thumbnails,
    @SerialName("localized")
    val localized: Localized,
    @SerialName("country")
    val country: String? = null,
    @SerialName("defaultLanguage")
    val defaultLanguague: String? = null
)
