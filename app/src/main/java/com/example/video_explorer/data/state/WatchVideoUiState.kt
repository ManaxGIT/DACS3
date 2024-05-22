package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo

sealed interface WatchVideoUiState {
    data class Success(var youtubeVideoItem: VideoItem) : WatchVideoUiState
    object Error : WatchVideoUiState
    object Loading : WatchVideoUiState
}