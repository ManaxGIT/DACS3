package com.example.video_explorer.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.video_explorer.YoutubeVideoApplication
import com.example.video_explorer.data.state.HomeScreenUiState
import com.example.video_explorer.data.state.WatchVideoUiState
import com.example.video_explorer.data.YoutubeVideoRepository
import com.example.video_explorer.data.state.SignInUiState
import com.example.video_explorer.data.user.UserData
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeChannel
import com.example.video_explorer.data.youtubeData.parts.VideoStatisticsResponse
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.ClassCastException

class YoutubeViewModel(
    private val youtubeVideoRepository: YoutubeVideoRepository
): ViewModel() {
    var homeScreenUiState: HomeScreenUiState by mutableStateOf(HomeScreenUiState.Loading)
        private set
    var watchVideoUiState: WatchVideoUiState by mutableStateOf(WatchVideoUiState.Loading)
        private set
    var signInUiState: SignInUiState by mutableStateOf(SignInUiState.NotSignedIn())
        private set

    init {

        getHomeVideoList(searchString = "lap trinh", maxResult = 6)
//        getHomeVideoListChannel()
//        ratingVideo(videoId = "YQHsXMglC9A", rating="like")
    }

    fun onSignInResult(signInResult: SignInUiState?) {
        if (signInResult == null) {
            signInUiState = SignInUiState.NotSignedIn()
        } else
            signInUiState = SignInUiState.SignedIn(user = (signInResult as SignInUiState.SignedIn).user)
    }
    fun setSignInUiState(user: UserData) {
        signInUiState = SignInUiState.SignedIn(user = user)
    }
    fun resetSignInUiState() {
        signInUiState = SignInUiState.NotSignedIn()
    }

    fun setWatchScreenUiStateToSuccess(videoItem: VideoItem) {
        this.watchVideoUiState = WatchVideoUiState.Success(youtubeVideoItem = videoItem)
        viewModelScope.launch {
            try {
                val currentSuccessWatchVideoUiState =
                    (watchVideoUiState as WatchVideoUiState.Success)
                val videoId = currentSuccessWatchVideoUiState.youtubeVideoItem.searchResponseId.id
                val commentList = youtubeVideoRepository.getVideoCommentList(videoId = videoId)
                currentSuccessWatchVideoUiState.setCommentList(commentList = commentList)

                val videoDetails = youtubeVideoRepository.getAdditionalDetails(videoId = videoId)
                currentSuccessWatchVideoUiState.setDescription(videoDetails.items[0].snippet.description)
                if(videoDetails.items[0].snippet.tags != null) {
                    currentSuccessWatchVideoUiState.setTags(videoDetails.items[0].snippet.tags!!)
                }
            } catch (e: Exception) {
//                watchVideoUiState = WatchVideoUiState.Error(errorNote = e.toString())
                Log.i("ex_loadDescription", e.toString())
            }
        }
    }

//    suspend fun getCommentList(videoId: String): YoutubeVideoComment? {
//        return try {
//            youtubeVideoRepository.getVideoCommentList((watchVideoUiState as WatchVideoUiState.Success).youtubeVideoItem.searchResponseId.id)
//        } catch (e: ClassCastException) {
//            Log.i("ex_getComment", "getComment executed when watchScreenUiState is Error/Loading")
//            null
//        } catch (e: HttpException) {
//            Log.i("ex_getComment", "Comment function is turned off in this video")
//            null
//        } catch (e: Exception) {
//            Log.i("ex_getComment", e.toString())
//            null
//        }
//    }

    suspend fun getChannelDetails(channelId: String): YoutubeChannel? {
        try {
            Log.i("ex_mess", "ViewModel getChannelDetails Id = ${channelId}")
            return youtubeVideoRepository.getChannelDetails(channelId = channelId)
        } catch (e: Exception) {
            Log.i("ex_channel", e.toString())
            return null
        }
    }

    suspend fun getVideoStatistics(videoId: String): VideoStatisticsResponse? {
        try {
            return youtubeVideoRepository.getVideoStatistics(videoId = videoId)
        } catch (e: Exception) {
            Log.i("ex_statistics", e.toString())
            return null
        }
    }

    fun getHomeVideoList(searchString: String = "", maxResult: Int = 20) {
        viewModelScope.launch {
            Log.i("ex_mess", "ViewModel getHomeVideoList Run Start")
            try {
                homeScreenUiState = HomeScreenUiState.Loading

                val searchResult = youtubeVideoRepository.getSearchVideo(searchString, maxResult = maxResult)
//                var videoList: YoutubeVideo = youtubeVideoRepository.getVideoDetails("MV8moKp1Wxw,NESs1KPmtKM,7pFAqHpLIHM,7lCDEYXw3mM,EoNOWVYKyo0,RyTb5genMmE,D7obfQ26V1M,-ljpcKRJdA8,C3GouGa0noM")
                homeScreenUiState = HomeScreenUiState.Success(videoList = searchResult, isChannelLoaded = false, isStatisticsLoaded = false)
                searchResult.items.forEach{ video->
                    val youtubeChannel = getChannelDetails(video.videoSnippet.channelId)
                    if (youtubeChannel != null) {
                        video.channel = youtubeChannel.items[0]
                    }
                }
                homeScreenUiState = HomeScreenUiState.Success(videoList = searchResult, isChannelLoaded = true, isStatisticsLoaded = false)
                searchResult.items.forEach{ video->
                    val videoStatisticsResponse = getVideoStatistics(video.searchResponseId.id)
                    if (videoStatisticsResponse != null) {
                        video.statistics = videoStatisticsResponse.items[0].videoStatistics
                    }
                }
                homeScreenUiState = HomeScreenUiState.Success(videoList = searchResult, isChannelLoaded = true, isStatisticsLoaded = true)




                Log.i("ex_mess", "ViewModel getHomeVideoList Run Success")
            } catch (e: IOException) {
                homeScreenUiState = HomeScreenUiState.Error("No Internet Connection")
                Log.i("ex_mess getHomeVideoList", e.toString())
            } catch (e: Exception) {
                homeScreenUiState = HomeScreenUiState.Error()
                Log.i("ex_mess4", e.toString())
            }
            Log.i("ex_mess", "ViewModel getHomeVideoList Run End")
        }
    }

    fun ratingVideo(videoId: String, rating: String) {
        viewModelScope.launch {
            try {
                Log.i("ex_rating", "videoId: $videoId")
                youtubeVideoRepository.rateVideo(videoId = videoId, rating = rating)
            } catch (e: Exception) {
                Log.i("ex_rating", e.stackTraceToString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            Log.i("ex_mess", "ViewModel Factory Run Start")
            try {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as YoutubeVideoApplication)
                    val youtubeVideoRepository = application.container.youtubeVideoRepository
                    YoutubeViewModel(youtubeVideoRepository = youtubeVideoRepository)
                }
            } catch (e: ClassCastException) {
                Log.i("ex_mess", e.toString())
            }
            Log.i("ex_mess", "ViewModel Factory Run End")
        }
    }
}