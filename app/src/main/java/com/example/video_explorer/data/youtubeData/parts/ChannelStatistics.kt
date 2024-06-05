package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelStatistics(
    @SerialName("hiddenSubscriberCount")
    val hiddenSubscriberCount: Boolean,
    @SerialName("subscriberCount")
    val subscriberCount: String,
    @SerialName("videoCount")
    val videoCount: String,
    @SerialName("viewCount")
    val viewCount: String
)