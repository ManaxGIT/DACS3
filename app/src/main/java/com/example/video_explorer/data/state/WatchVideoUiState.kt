package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment

sealed interface WatchVideoUiState {
    data class Success(var youtubeVideoItem: VideoItem) : WatchVideoUiState {
        var youtubeVideoComment: YoutubeVideoComment? = null
        fun setCommentList(commentList: YoutubeVideoComment?) {
            youtubeVideoComment = commentList
        }
        fun setDescription(description: String) {
            youtubeVideoItem.videoSnippet.description = description
        }
    }
    object Error : WatchVideoUiState
    object Loading : WatchVideoUiState
}