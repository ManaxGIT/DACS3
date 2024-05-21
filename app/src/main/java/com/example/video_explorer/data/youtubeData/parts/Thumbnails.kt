package com.example.video_explorer.data.youtubeData.parts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("default")
    val default: Default = Default(),
    @SerialName("medium")
    val medium: Medium = Medium(),
    @SerialName("high")
    val high: High = High(),
    @SerialName("standard")
    val standard: Standard = Standard(),
    @SerialName("maxres")
    val maxRes: MaxRes = MaxRes()
)

@Serializable
data class Default(
    @SerialName("height")
    val height: Int = 90,
    @SerialName("url")
    val url: String = "https://i.ytimg.com/vi/7lCDEYXw3mM/default.jpg",
    @SerialName("width")
    val width: Int = 120
)

@Serializable
data class Medium(
    @SerialName("height")
    val height: Int = 180,
    @SerialName("url")
    val url: String = "https://i.ytimg.com/vi/7lCDEYXw3mM/mqdefault.jpg",
    @SerialName("width")
    val width: Int = 320
)

@Serializable
data class High(
    @SerialName("height")
    val height: Int = 360,
    @SerialName("url")
    val url: String = "https://i.ytimg.com/vi/7lCDEYXw3mM/hqdefault.jpg",
    @SerialName("width")
    val width: Int = 480
)

@Serializable
data class Standard(
    @SerialName("height")
    val height: Int = 480,
    @SerialName("url")
    val url: String = "https://i.ytimg.com/vi/7lCDEYXw3mM/sddefault.jpg",
    @SerialName("width")
    val width: Int = 640
)

@Serializable
data class MaxRes(
    @SerialName("height")
    val height: Int = 720,
    @SerialName("url")
    val url: String = "https://i.ytimg.com/vi/7lCDEYXw3mM/maxresdefault.jpg",
    @SerialName("width")
    val width: Int = 1280
)


