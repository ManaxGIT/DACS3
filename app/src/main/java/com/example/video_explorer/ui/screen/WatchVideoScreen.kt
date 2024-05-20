package com.example.video_explorer.ui.screen

import com.example.video_explorer.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.video_explorer.data.youtubeData.Item
import com.example.video_explorer.data.youtubeData.Localized
import com.example.video_explorer.data.youtubeData.PageInfo
import com.example.video_explorer.data.youtubeData.Snippet
import com.example.video_explorer.data.youtubeData.Statistics
import com.example.video_explorer.data.youtubeData.Thumbnails
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.ui.YouTubeVideoPlayer

@Composable
fun WatchVideoScreen(
    video: YoutubeVideo
) {
    Column(

    ) {
        YouTubeVideoPlayer(
            videoId = video.items[0].id,
            lifecycleOwner = LocalLifecycleOwner.current
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = video.items[0].snippet.title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            LikeDislikeIcon(R.drawable.thumb_up, video.items[0].statistics.likeCount)
            Spacer(modifier = Modifier.padding(16.dp))
            LikeDislikeIcon(R.drawable.thumb_down)
        }

    }
}

@Composable
fun LikeDislikeIcon(iconId: Int, count: String = "-1") {
    Row {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Gray,
            modifier = androidx.compose.ui.Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
        if (!count.equals("-1"))
            Text(
                text = count.toString(),
                color = Color.Gray
            )

    }
}



@Preview(showBackground = true)
@Composable
fun WatchVideoScreenPreview() {
    val thumbnails = Thumbnails()

    val localized = Localized(
        title = "Google I/O 101: Q&A On Using Google APIs",
        description = "Antonio Fuentes speaks to us and takes questions on working with Google APIs and OAuth 2.0."
    )

    val snippet = Snippet(
        publishedAt = "2012-06-20T23:12:38Z",
        channelId = "UC_x5XG1OV2P6uZZ5FSM9Ttw",
        title = "Google I/O 101: Q&A On Using Google APIs",
        description = "Antonio Fuentes speaks to us and takes questions on working with Google APIs and OAuth 2.0.",
        thumbnails = thumbnails,
        channelTitle = "Google for Developers",
        tags = listOf("api", "gdl", "i-o"),
        categoryId = "28",
        liveBroadcastContent = "none",
        localized = localized
    )

    val statistics = Statistics(
        viewCount = "13255",
        likeCount = "85",
        favoriteCount = "0",
        commentCount = "10"
    )

    val videoItem = Item(
        etag = "",
        kind = "",
        id = "7lCDEYXw3mM",
        snippet = snippet,
        statistics = statistics,
    )

    val pageInfo = PageInfo(
        totalResults = 1,
        resultsPerPage = 1
    )

// Finally, create the YoutubeVideo object
    val fakeVideo = YoutubeVideo(
        etag = "",
        kind = "",
        items = listOf(videoItem),
        pageInfo = pageInfo
    )
    WatchVideoScreen(video = fakeVideo)
}