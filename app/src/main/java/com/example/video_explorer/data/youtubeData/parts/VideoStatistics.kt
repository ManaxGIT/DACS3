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
    @SerialName("contentDetails")
    val contentDetails: AdditionalContentDetails
)


@Serializable
data class VideoStatistics(
    @SerialName("commentCount")
    val commentCount: String = "0",
    @SerialName("favoriteCount")
    val favoriteCount: String,
    @SerialName("likeCount")
    val likeCount: String = "1",
    @SerialName("viewCount")
    val viewCount: String,
)


@Serializable
data class AdditionalContentDetails(
    @SerialName("duration")
    val duration: String,
    @SerialName("dimension")
    val dimension: String? = null,
    @SerialName("definition")
    val definition: String? = null,
    @SerialName("caption")
    val caption: String? = null,
    @SerialName("licensedContent")
    val licensedContent: Boolean? = null,
    @SerialName("regionRestriction")
    val regionRestriction: RegionRestriction? = null,
    @SerialName("contentRating")
    val contentRating: Unit? = null,
    @SerialName("projection")
    val projection: String? = null,
    @SerialName("hasCustomThumbnail")
    val contentRhasCustomThumbnailating: Boolean? = null,

)

@Serializable
data class RegionRestriction(
    @SerialName("allowed")
    val allowed: List<String>? = null,
    @SerialName("blocked")
    val blocked: List<String>? = null,
)