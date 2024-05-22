package com.example.video_explorer.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.video_explorer.data.state.HomeScreenUiState
import com.example.video_explorer.data.state.WatchVideoUiState
import com.example.video_explorer.data.youtubeData.ChannelItem
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.parts.ChannelSnippet
import com.example.video_explorer.data.youtubeData.parts.ChannelStatistics
import com.example.video_explorer.data.youtubeData.parts.Default
import com.example.video_explorer.data.youtubeData.parts.Localized
import com.example.video_explorer.data.youtubeData.parts.PageInfo
import com.example.video_explorer.data.youtubeData.parts.Snippet
import com.example.video_explorer.data.youtubeData.parts.Thumbnails
import com.example.video_explorer.data.youtubeData.parts.TopicDetails
import com.example.video_explorer.data.youtubeData.parts.VideoStatistics
import com.example.video_explorer.model.YoutubeViewModel
import com.example.video_explorer.ui.Screen
import okio.utf8Size
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@Composable
fun HomeScreen(
    youtubeViewModel: YoutubeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Log.i("ex_mess", "HomeScreen Run Start")
    val homeScreenUiState = youtubeViewModel.homeScreenUiState
    when(homeScreenUiState) {
        is HomeScreenUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
            Log.i("ex_mess", "HomeScreen Is Loading")
        }
        is HomeScreenUiState.Success -> {
            Log.i("ex_mess", "HomeScreen Is Success")
            HomeScreenList(
                homeScreenUiState = homeScreenUiState,
                youtubeViewModel = youtubeViewModel,
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
    homeScreenUiState: HomeScreenUiState,
    youtubeViewModel: YoutubeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items((homeScreenUiState as HomeScreenUiState.Success).videoList.items) { videoItem ->
            VideoItem(
                video = videoItem,
                onClick = {
                    navController.navigate(Screen.WatchVideo.name)
                    youtubeViewModel.setWatchScreenUiState(WatchVideoUiState.Success(videoItem))
                }
            )
        }
    }
}


@Composable
fun VideoItem(video: VideoItem, onClick: () -> Unit, modifier: Modifier= Modifier) {
    val channel = video.channelItem!!
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(onClick = onClick)

    ) {
        Image(
            painter = rememberAsyncImagePainter(model = video.snippet.thumbnails.medium.url),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier.padding(
                start = 8.dp, top = 8.dp
            )
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = channel.snippet.thumbnails.default.url),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                var videoTitle = video.snippet.title
                if (videoTitle.utf8Size() > 70)
                    videoTitle = "${videoTitle.substring(0,70)}..."

                var channelTitle = "${channel.snippet.title} · ${calculateView(video.statistics.viewCount)} · ${calculateTime(video.snippet.publishedAt)}"
                Text(
                    text = videoTitle,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
                Text(text = channelTitle)
            }
        }
    }
}

private fun calculateView(view: String): String {
    val viewNumber = view.toDouble()
    if(viewNumber >= 1000000000)
        return "${String.format("%.1f", viewNumber / 1000000000)} T lượt xem"
    else if (viewNumber >= 10000000)
        return "${(viewNumber / 1000000).toInt()} Tr lượt xem"
    else if (viewNumber >= 1000000)
        return "${String.format("%.1f", viewNumber / 1000000)} Tr lượt xem"
    else if (viewNumber >= 10000)
        return "${(viewNumber / 1000).toInt()} N lượt xem"
    else if (viewNumber >= 1000)
        return "${String.format("%.1f", viewNumber / 1000)} N lượt xem"
    else
        return view
}

private fun calculateTime(start: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    formatter.timeZone = TimeZone.getTimeZone("UTC")

    val startTime = formatter.parse(start)
    val currentTime = Date()

    val diff = (currentTime.time - startTime.time) / 1000

    if (currentTime.year - startTime.year >= 1 && diff / (60*60) >= (24*30*12))
        return "${currentTime.year - startTime.year} năm trước"
    else if ((currentTime.month - startTime.month + 12) % 12 >= 1 && diff / (60*60) >= (24*30))
        return "${(currentTime.month - startTime.month + 12) % 12} tháng trước"
    else if ((currentTime.date - startTime.date + 31) % 31 >= 1 && diff / (60*60) >= 24)
        return "${(currentTime.date - startTime.date + 31) % 31} ngày trước"
    else if (diff / (60*60) >= 1)
        return "${diff / (60*60)} giờ trước"
    else
        return "${(diff % (60 * 60)) / (60)} phút trước"
}




@Preview(showBackground = true)
@Composable
fun videoItemPreview() {
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
        likeCount = "85",
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
            subscriberCount = "0",
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
    
    com.example.video_explorer.ui.screen.VideoItem(video = fakeVideo.items[0], onClick = { /*TODO*/ })
}