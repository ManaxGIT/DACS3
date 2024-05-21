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
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.ClassCastException

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
            }.await()
            async {
                displayVideo((homeScreenUiState as HomeScreenUiState.Success).videoList.items[0])
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


    suspend private fun getHomeVideoList() {
        Log.i("ex_mess", "ViewModel getHomeVideoList Run Start")
        try {
            var videoList: YoutubeVideo = youtubeVideoRepository.getVideoDetails("7lCDEYXw3mM,EoNOWVYKyo0,RyTb5genMmE")
//            delay(6000)
            homeScreenUiState = HomeScreenUiState.Success(
                videoList = videoList
            )
            Log.i("ex_mess", "ViewModel getHomeVideoList Run End")
        } catch (e: IOException) {
            homeScreenUiState = HomeScreenUiState.Error("No Internet Connection")
            Log.i("ex_mess3", e.toString())
        } catch (e: Exception) {
            homeScreenUiState = HomeScreenUiState.Error()
            Log.i("ex_mess4", e.toString())
        }
    }

    suspend private fun displayVideo(video: VideoItem) {
        Log.i("ex_mess", "ViewModel displayVideo Run Start")
        try {
            watchVideoUiState = WatchVideoUiState.Success(video)
        }  catch (e: Exception) {
            Log.i("ex_mess", "No Internet At ViewModel")
            homeScreenUiState = HomeScreenUiState.Error()
            watchVideoUiState = WatchVideoUiState.Error
        }
        Log.i("ex_mess", "ViewModel displayVideo Run End")
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