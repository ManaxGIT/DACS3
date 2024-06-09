package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoStatisticsResponse(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("items")
    val items: List<VideoStatisticsResponseItem>,
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)

@Serializable
data class VideoStatisticsResponseItem(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("statistics")
    val videoStatistics: VideoStatistics,
)


@Serializable
data class VideoStatistics(
    @SerialName("commentCount")
    val commentCount: String = "0",
    @SerialName("favoriteCount")
    val favoriteCount: String,
    @SerialName("likeCount")
    val likeCount: String,
    @SerialName("viewCount")
    val viewCount: String
)
