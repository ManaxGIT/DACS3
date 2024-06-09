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
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import com.example.video_explorer.data.youtubeData.parts.VideoStatistics
import com.example.video_explorer.data.youtubeData.parts.VideoStatisticsResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
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

        getHomeVideoList()
//        getHomeVideoListChannel()
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
            val videoId = (watchVideoUiState as WatchVideoUiState.Success).youtubeVideoItem.searchResponseId.id
            val commentList = youtubeVideoRepository.getVideoCommentList(videoId = videoId)
            (watchVideoUiState as WatchVideoUiState.Success).setCommentList(commentList = commentList)

//            val videoDetails = youtubeVideoRepository.getVideoDetails(videoId = videoId)
//            (watchVideoUiState as WatchVideoUiState.Success).setDescription(videoDetails.items[0].)
            Log.i("ex_getComment", "getComment Run End")
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
            Log.i("ex_mess getChannelDetails", e.toString())
            return null
        }
    }

    suspend fun getVideoStatistics(videoId: String): VideoStatisticsResponse? {
        try {
            return youtubeVideoRepository.getVideoStatistics(videoId = videoId)
        } catch (e: Exception) {
            Log.i("ex_mess getChannelDetails", e.toString())
            return null
        }
    }

    private fun getHomeVideoList() {
        viewModelScope.launch {
            Log.i("ex_mess", "ViewModel getHomeVideoList Run Start")
            try {

                val searchResult = youtubeVideoRepository.getSearchVideo("")
                var query = ""

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
            val a = ""
            Log.i("ex_mess", "ViewModel getHomeVideoList Run End")
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