package com.example.video_explorer.data.youtubeData

import com.example.video_explorer.data.youtubeData.parts.Localized
import com.example.video_explorer.data.youtubeData.parts.PageInfo
import com.example.video_explorer.data.youtubeData.parts.Thumbnails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoAddtitionalDetails(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("pageInfo")
    val pageInfo: PageInfo,
    @SerialName("items")
    val items: List<AdditionalDetails>
)

@Serializable
data class AdditionalDetails(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("snippet")
    val snippet: AdditionalDetailsSnippet,
)

@Serializable
data class AdditionalDetailsSnippet(
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
    @SerialName("tags")
    val tags: List<String>? = null,
    @SerialName("categoryId")
    val categoryId: String,
    @SerialName("liveBroadcastContent")
    val liveBroadcastContent: String,
    @SerialName("defaultLanguage")
    val defaultLanguage: String? = null,
    @SerialName("localized")
    val localized: Localized,
    @SerialName("defaultAudioLanguage")
    val defaultAudioLanguage: String? = null
)
