package com.example.video_explorer.ui.videoplayer

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun YouTubeVideoPlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier,
    toggleFullScreen: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var youTubePlayer: YouTubePlayer? by remember { mutableStateOf(null) }
    var currentTime by remember{ mutableStateOf(0f) }


    Box(modifier = modifier) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                createYouTubePlayerView(context, lifecycleOwner) { player ->
                    youTubePlayer = player
                    player.loadVideo(videoId, 0f)
                }
            }
        )
//        Button(onClick = {
////            coroutineScope.launch {
////                if(youTubePlayer != null) {
////                    Log.i("ex_player", "skip")
////                    currentTime += 10f
////                    youTubePlayer!!.seekTo(currentTime) // Skip to 10 seconds
////                }
////            }
//            toggleFullScreen()
//        }) {
//            Text("Skip to 2 minutes")
//        }

        LaunchedEffect(youTubePlayer) {
            while (youTubePlayer != null) {
                youTubePlayer?.let {
                    currentTime += 1f
                }
                delay(1000) // Update every second
            }
        }
    }
}


private fun createYouTubePlayerView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onReady: (YouTubePlayer) -> Unit
): YouTubePlayerView {
    return YouTubePlayerView(context).apply {
        lifecycleOwner.lifecycle.addObserver(this)

        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                onReady(youTubePlayer)
            }
        })
    }
}

private fun formatTime(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val remainingSeconds = (seconds % 60).toInt()
    return String.format("%02d:%02d", minutes, remainingSeconds)
}