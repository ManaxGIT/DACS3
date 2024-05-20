package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo

sealed interface WatchVideoUiState {
    data class Success(var youtubeVideoItem: VideoItem) : WatchVideoUiState {
        fun setWatchVideo(video: VideoItem) {
            youtubeVideoItem = video
        }
    }
    object Error : WatchVideoUiState
    object Loading : WatchVideoUiState
}