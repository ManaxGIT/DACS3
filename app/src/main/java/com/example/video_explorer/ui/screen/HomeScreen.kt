package com.example.video_explorer.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.video_explorer.data.state.HomeScreenUiState
import com.example.video_explorer.data.state.WatchVideoUiState
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.ui.Screen

@Composable
fun HomeScreen(
    watchVideoUiState: WatchVideoUiState,
    homeScreenUiState: HomeScreenUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Log.i("ex_mess", "HomeScreen Run Start")
//    when(watchVideoUiState) {
//        is WatchVideoUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
//        is WatchVideoUiState.SignedIn -> ErrorScreen(errorNote = watchVideoUiState.youtubeVideo.items[0].snippet.title)
//        is WatchVideoUiState.Error -> ErrorScreen(errorNote = "Something Unusual Happened")
//
    when(homeScreenUiState) {
        is HomeScreenUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
            Log.i("ex_mess", "HomeScreen Is Loading")
        }
        is HomeScreenUiState.Success -> {
            Log.i("ex_mess", "HomeScreen Is Success")
            HomeScreenList(
                watchVideoUiState = watchVideoUiState,
                homeScreenUiState = homeScreenUiState,
                navController = navController
            )
        }
        is HomeScreenUiState.Error -> {
            Log.i("ex_mess", "HomeScreen Is Error")
            ErrorScreen(errorNote = homeScreenUiState.errorNote)
        }
    }
}

@Composable
private fun HomeScreenList(
    watchVideoUiState: WatchVideoUiState,
    homeScreenUiState: HomeScreenUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items((homeScreenUiState as HomeScreenUiState.Success).videoList.items) { videoItem ->
            VideoItem(
                video = videoItem,
                onClick = {
                    (watchVideoUiState as WatchVideoUiState.Success).setWatchVideo(video = videoItem)
                    navController.navigate(Screen.WatchVideo.name)
                }
            )
        }
    }
}

@Composable
fun VideoItem(video: VideoItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
            .clickable(onClick = onClick)

    ) {
        Image(
            painter = rememberAsyncImagePainter(model = video.snippet.thumbnails.medium.url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)            ,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = video.snippet.title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
    }
}