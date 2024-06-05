package com.example.video_explorer.data.youtubeData

import com.example.video_explorer.data.youtubeData.parts.CommentSnippet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentItem(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("snippet")
    val snippet: CommentSnippet,
)