package com.example.video_explorer.data.youtubeData.parts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentSnippet(
    @SerialName("channelId")
    val channelId: String,
    @SerialName("videoId")
    val videoId: String,
    @SerialName("topLevelComment")
    val topLevelComment: TopLevelComment,
    @SerialName("canReply")
    val canReply: Boolean,
    @SerialName("totalReplyCount")
    val totalReplyCount: Int,
    @SerialName("isPublic")
    val isPublic: Boolean,
)

@Serializable
data class TopLevelComment(
    @SerialName("kind")
    val kind: String,
    @SerialName("etag")
    val etag: String,
    @SerialName("id")
    val id: String,
    @SerialName("snippet")
    val snippet: TopLevelCommentSnippet,
)

@Serializable
data class TopLevelCommentSnippet (
    @SerialName("channelId")
    val channelId: String,
    @SerialName("videoId")
    val videoId: String,
    @SerialName("textDisplay")
    val textDisplay: String,
    @SerialName("textOriginal")
    val textOriginal: String,
    @SerialName("authorDisplayName")
    val authorDisplayName: String,
    @SerialName("authorProfileImageUrl")
    val authorProfileImageUrl: String,
    @SerialName("authorChannelUrl")
    val authorChannelUrl: String,
    @SerialName("authorChannelId")
    val authorChannelId: AuthorChannelId,
    @SerialName("canRate")
    val canRate: Boolean,
    @SerialName("viewerRating")
    val viewerRating: String,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
)

@Serializable
data class AuthorChannelId (
    @SerialName("value")
    val value: String
)
