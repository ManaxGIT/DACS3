package com.example.video_explorer.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import com.example.video_explorer.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.video_explorer.data.state.WatchVideoUiState
import com.example.video_explorer.data.youtubeData.ChannelItem
import com.example.video_explorer.data.youtubeData.parts.Localized
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.parts.PageInfo
import com.example.video_explorer.data.youtubeData.parts.Snippet
import com.example.video_explorer.data.youtubeData.parts.VideoStatistics
import com.example.video_explorer.data.youtubeData.parts.Thumbnails
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.parts.ChannelSnippet
import com.example.video_explorer.data.youtubeData.parts.ChannelStatistics
import com.example.video_explorer.data.youtubeData.parts.Default
import com.example.video_explorer.data.youtubeData.parts.TopicDetails
import com.example.video_explorer.ui.YouTubeVideoPlayer
import com.example.video_explorer.ui.render.calculateLike
import com.example.video_explorer.ui.render.calculateSubscriber
import com.example.video_explorer.ui.render.calculateTime
import com.example.video_explorer.ui.render.calculateView
import com.example.video_explorer.ui.render.getFirstTag

@Composable
fun WatchVideoScreen(
    watchVideoUiState: WatchVideoUiState
) {
    Log.i("ex_", "WatchVideoScreen Run Start")
    when(watchVideoUiState) {
        is WatchVideoUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxWidth())
        is WatchVideoUiState.Success -> WatchVideo(video = watchVideoUiState.youtubeVideoItem)
        is WatchVideoUiState.Error -> ErrorScreen(errorNote = "Watch Video Screen Is Error")
    }
}

@Composable
fun WatchVideo(
    video: VideoItem
) {
    Column(

    ) {
        YouTubeVideoPlayer(
            videoId = video.id,
            lifecycleOwner = LocalLifecycleOwner.current
        )
        Text(
            text = video.snippet.title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )
        Text(
            text = "${calculateView(video.statistics.viewCount)}  ${calculateTime(video.snippet.publishedAt)} ${getFirstTag(video)} ...xem thêm",
            modifier = Modifier.padding(start = 8.dp)
        )
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = video.channelItem!!.snippet.thumbnails.default.url),
                contentDescription = null,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = video.channelItem!!.snippet.title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "${calculateSubscriber(video.channelItem!!.statistics.subscriberCount)}")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Right) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .width(56.dp)
                        .height(30.dp)
                ) {
                    Text(
                        text = "Đăng ký",
                        style = TextStyle(fontSize = 6.sp)
                    )
                }
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            LikeDislikeIcon(R.drawable.thumb_up, video.statistics.likeCount)
            Spacer(modifier = Modifier.padding(8.dp))
            LikeDislikeIcon(R.drawable.thumb_down)
        }

    }
}

@Composable
fun LikeDislikeIcon(iconId: Int, count: String = "-1", modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 8.dp, top = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Gray,
            modifier = androidx.compose.ui.Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.padding(2.dp))
        if (!count.equals("-1"))
            Text(
                text = calculateLike(count),
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
        title = "Google I/O 101: Q&A On Using Google APIs Quốc hội sẽ phê chuẩn miễn nhiệm bộ trưởng Bộ Công an đối với đại tướng Tô Lâm\n",
        description = "Antonio Fuentes speaks to us and takes questions on working with Google APIs and OAuth 2.0.",
        thumbnails = thumbnails,
        channelTitle = "Google for Developers",
        tags = listOf("api", "gdl", "i-o"),
        categoryId = "28",
        liveBroadcastContent = "none",
        localized = localized
    )

    val statistics = VideoStatistics(
        viewCount = "13255",
        likeCount = "8500000000",
        favoriteCount = "0",
        commentCount = "10"
    )
    val mockChannel = ChannelItem(
        kind = "youtube#channel",
        etag = "mock_etag",
        id = "UCMockedChannelId",
        snippet = ChannelSnippet(
            title = "Mocked Channel Titleeeeeeee",
            description = "This is a mock channel description",
            customUrl = "mocked_custom_url",
            publishedAt = "2024-05-21T10:20:00Z", // Replace with desired publish date
            thumbnails = Thumbnails(
                default = Default(url = "https://yt3.ggpht.com/ebid6d64FrF3wZMqdjxRpoKVl-FpFJv4ovUbG0wTMXJ7EkuC5Qn7cRszvLvxXBwrbDlLMYu76pc=s88-c-k-c0x00ffffff-no-rj")
            ),
            localized = Localized("","")
        ),
        statistics = ChannelStatistics(
            hiddenSubscriberCount = false,
            subscriberCount = "1234491",
            videoCount = "0",
            viewCount = "0"
        ),
        topicDetails = TopicDetails(
            topicCategories = listOf("",""),
            topicIds = listOf("","")
        )
    )

    val videoItem = VideoItem(
        etag = "",
        kind = "",
        id = "7lCDEYXw3mM",
        snippet = snippet,
        statistics = statistics,
        channelItem = mockChannel
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
    WatchVideo(video = fakeVideo.items[0])
}