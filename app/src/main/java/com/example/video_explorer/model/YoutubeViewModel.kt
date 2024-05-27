package com.example.video_explorer.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
        runBlocking {
            async {
                getHomeVideoList()
            }
        }
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
        runBlocking {
            (watchVideoUiState as WatchVideoUiState.Success).setCommentList(getCommentList())
            Log.i("ex_getComment", "getComment Run End")
        }
    }

    suspend fun getCommentList(): YoutubeVideoComment? {
        return try {
            youtubeVideoRepository.getVideoCommentList((watchVideoUiState as WatchVideoUiState.Success).youtubeVideoItem.id)
        } catch (e: ClassCastException) {
            Log.i("ex_getComment", "getComment executed when watchScreenUiState is Error/Loading")
            null
        } catch (e: HttpException) {
            Log.i("ex_getComment", "Comment function is turned off in this video")
            null
        } catch (e: Exception) {
            Log.i("ex_getComment", e.toString())
            null
        }
    }

    suspend fun getChannelDetails(channelId: String): YoutubeChannel? {
        try {
            Log.i("ex_mess", "ViewModel getChannelDetails Id = ${channelId}")
            return youtubeVideoRepository.getChannelDetails(channelId = channelId)
        } catch (e: Exception) {
            Log.i("ex_mess getChannelDetails", e.toString())
            return null
        }
    }

    suspend private fun getHomeVideoList() {
        Log.i("ex_mess", "ViewModel getHomeVideoList Run Start")
        try {
            var videoList: YoutubeVideo = youtubeVideoRepository.getVideoDetails("NESs1KPmtKM,7pFAqHpLIHM,7lCDEYXw3mM,EoNOWVYKyo0,RyTb5genMmE,D7obfQ26V1M,-ljpcKRJdA8,C3GouGa0noM")
//            delay(6000)
            videoList.items.forEach{ video->
                val youtubeChannel = getChannelDetails(video.snippet.channelId)
                if (youtubeChannel != null) {
                    video.channelItem = youtubeChannel.items[0]
                }
            }

            homeScreenUiState = HomeScreenUiState.Success(
                videoList = videoList
            )
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