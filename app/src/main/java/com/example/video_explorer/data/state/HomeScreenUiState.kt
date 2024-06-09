package com.example.video_explorer.data.state

import com.example.video_explorer.data.youtubeData.YoutubeVideo


sealed interface HomeScreenUiState {
    data class Success(val videoList: YoutubeVideo, val isChannelLoaded: Boolean, val isStatisticsLoaded: Boolean) : HomeScreenUiState
    data class Error(val errorNote:String = "Something Unsual Happened") : HomeScreenUiState
    object Loading : HomeScreenUiState
}