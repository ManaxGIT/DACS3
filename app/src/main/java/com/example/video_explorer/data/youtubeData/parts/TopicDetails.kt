package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDetails(
    @SerialName("topicCategories")
    val topicCategories: List<String>? = null,
    @SerialName("topicIds")
    val topicIds: List<String>? = null
)