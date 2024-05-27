package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment

sealed interface WatchVideoUiState {
    data class Success(var youtubeVideoItem: VideoItem, var youtubeVideoComment: YoutubeVideoComment? = null) : WatchVideoUiState {
        fun setCommentList(commentList: YoutubeVideoComment?) {
            youtubeVideoComment = commentList
        }
    }
    object Error : WatchVideoUiState
    object Loading : WatchVideoUiState
}