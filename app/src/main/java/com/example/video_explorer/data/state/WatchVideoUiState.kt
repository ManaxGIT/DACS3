package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.YoutubeVideo

sealed interface WatchVideoUiState {
    data class Success(var youtubeVideo: YoutubeVideo) : WatchVideoUiState {
        fun setWatchVideo(video: YoutubeVideo) {
            youtubeVideo = video
        }
    }
    object Error : WatchVideoUiState
    object Loading : WatchVideoUiState
}