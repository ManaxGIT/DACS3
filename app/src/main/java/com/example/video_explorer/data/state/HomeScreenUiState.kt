package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo


sealed interface HomeScreenUiState {
    data class Success(val videoList: YoutubeVideo, val isChannelLoaded: Boolean, val isStatisticsLoaded: Boolean, var flag: Boolean = false) : HomeScreenUiState {
        fun expandVideoList(newList: List<VideoItem>) {
            videoList.items = videoList.items + newList
            updateToScreen()
        }

        fun updateToScreen() {
            flag = !flag
        }
    }
    data class Error(val errorNote:String = "Something Unsual Happened") : HomeScreenUiState
    object Loading : HomeScreenUiState
}