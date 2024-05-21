package com.example.video_explorer.data.youtubeData.parts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    @SerialName("embeddable")
    val embeddable: Boolean,
    @SerialName("license")
    val license: String,
    @SerialName("madeForKids")
    val madeForKids: Boolean,
    @SerialName("privacyStatus")
    val privacyStatus: String,
    @SerialName("publicStatsViewable")
    val publicStatsViewable: Boolean,
    @SerialName("uploadStatus")
    val uploadStatus: String
)