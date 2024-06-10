package com.example.video_explorer.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
import com.example.video_explorer.data.youtubeData.CommentItem
import com.example.video_explorer.data.youtubeData.SearchResponseId
import com.example.video_explorer.data.youtubeData.parts.Localized
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.parts.PageInfo
import com.example.video_explorer.data.youtubeData.parts.VideoSnippet
import com.example.video_explorer.data.youtubeData.parts.VideoStatistics
import com.example.video_explorer.data.youtubeData.parts.Thumbnails
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import com.example.video_explorer.data.youtubeData.parts.AuthorChannelId
import com.example.video_explorer.data.youtubeData.parts.ChannelSnippet
import com.example.video_explorer.data.youtubeData.parts.ChannelStatistics
import com.example.video_explorer.data.youtubeData.parts.CommentSnippet
import com.example.video_explorer.data.youtubeData.parts.Default
import com.example.video_explorer.data.youtubeData.parts.TopLevelComment
import com.example.video_explorer.data.youtubeData.parts.TopLevelCommentSnippet
import com.example.video_explorer.data.youtubeData.parts.TopicDetails
import com.example.video_explorer.ui.YouTubeVideoPlayer
import com.example.video_explorer.ui.render.calculateLike
import com.example.video_explorer.ui.render.calculateSubscriber
import com.example.video_explorer.ui.render.calculateTime
import com.example.video_explorer.ui.render.calculateView
import com.example.video_explorer.ui.render.getFirstTag
import com.example.video_explorer.ui.render.getRandomComment
import com.example.video_explorer.ui.render.reduceStringLength
import kotlinx.coroutines.launch

private sealed interface Sheet {
    object Description: Sheet
    object Comment: Sheet
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchVideoScreen(
    watchVideoUiState: WatchVideoUiState
) {
    Log.i("ex_", "WatchVideoScreen Run Start")
    when(watchVideoUiState) {
        is WatchVideoUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxWidth())
        is WatchVideoUiState.Success -> {
            val scaffoldState = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()
            var sheetContent: Sheet by remember { mutableStateOf(Sheet.Description) }

            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val localDensity = LocalDensity.current
            var videoHeightDp by remember { mutableStateOf(500.dp) }

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContent =  {
                    BottomSheetPart(
                        bottomSheetContent = sheetContent,
                        video = watchVideoUiState.youtubeVideoItem,
                        commentList = watchVideoUiState.youtubeVideoComment,
                        modifier = Modifier.height(screenHeight - videoHeightDp - 24.dp)
                    )
                },
                sheetPeekHeight = 0.dp,
                sheetDragHandle = { NewDragHandle() }
            ) {
                WatchVideo(
                    video = watchVideoUiState.youtubeVideoItem,
                    commentList = watchVideoUiState.youtubeVideoComment,
                    openDescription = { scope.launch {
                        sheetContent = Sheet.Description
                        scaffoldState.bottomSheetState.expand()

                    }},
                    openComment = { scope.launch {
                        sheetContent = Sheet.Comment
                        scaffoldState.bottomSheetState.expand()
                    }},
                    getHeightModifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            videoHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                        }
                )
            }

        }
        is WatchVideoUiState.Error -> ErrorScreen(errorNote = watchVideoUiState.errorNote)
    }
}

@Composable
fun WatchVideo(
    video: VideoItem,
    commentList: YoutubeVideoComment?,
    modifier: Modifier = Modifier,
    openDescription: () -> Unit,
    openComment: () -> Unit,
    getHeightModifier: Modifier

) {
    Column(
        modifier = modifier
    ) {
        YouTubeVideoPlayer(
            videoId = video.searchResponseId.id,
            lifecycleOwner = LocalLifecycleOwner.current,
            modifier = getHeightModifier
        )
        Column(
            modifier = modifier.padding(start = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()
                    .clickable { openDescription() }
            ) {
                Text(
                    text = reduceStringLength(video.videoSnippet.title, length = 85),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "${calculateView(video.statistics!!.viewCount)}  ${calculateTime(video.videoSnippet.publishedAt)} ${getFirstTag(video)} ...xem thêm",
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = video.channel!!.snippet.thumbnails.default.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = video.channel!!.snippet.title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = calculateSubscriber(video.channel!!.statistics.subscriberCount))
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
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Row(
                modifier = Modifier
            ) {
                LikeDislikeIcon(iconId = R.drawable.thumb_up, onClick = {}, count = video.statistics!!.likeCount)
                Spacer(modifier = Modifier.padding(8.dp))
                LikeDislikeIcon(iconId = R.drawable.thumb_down, onClick = {})
            }

            CommentBox(
                commentList = commentList,
                commentCount = video.statistics!!.commentCount,
                onOpenClick = { openComment() }
            )
        }

    }
}

@Composable
fun CommentBox(
    commentList: YoutubeVideoComment?,
    commentCount: String,
    modifier: Modifier = Modifier,
    onOpenClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp)
            .clickable { if (commentList != null) onOpenClick() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bình luận",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                    modifier = Modifier
                )
                if (commentList != null)
                    Text(
                        text = commentCount,
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(start = 8.dp, top = 3.dp)
                    )
            }

            if (commentList != null) {
                val randomComment = getRandomComment(commentList)
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = randomComment.snippet.topLevelComment.snippet.authorProfileImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = randomComment.snippet.topLevelComment.snippet.textOriginal)
                }
            } else {
                Text(
                    text = "Tính năng bình luận đã bị tắt.",
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

        }
    }
}

@Composable
fun LikeDislikeIcon(iconId: Int, count: String = "-1", onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 8.dp, top = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
                .clickable { onClick() }
        )
        Spacer(modifier = Modifier.padding(2.dp))
        if (!count.equals("-1"))
            Text(
                text = calculateLike(count),
                color = Color.Gray
            )
    }
}

@Composable
private fun BottomSheetPart(
    bottomSheetContent: Sheet,
    video: VideoItem,
    commentList: YoutubeVideoComment?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        when(bottomSheetContent) {
            is Sheet.Description -> {
                DecriptionBottomSheet(video = video)
            }
            is Sheet.Comment -> {
                CommentBottomSheet(commentList = commentList)
            }
        }
    }
}

@Composable
fun DecriptionBottomSheet(video: VideoItem) {
    Column {
        Text(
            text = "Nội dung mô tả",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.padding(8.dp)
        )
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.videoSnippet.title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                val upperTextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = calculateLike(video.statistics!!.likeCount),
                        style = upperTextStyle
                    )
                    Text(text = "Lượt thích")
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = video.statistics!!.viewCount,
                        style = upperTextStyle
                    )
                    Text(text = "Lượt xem")
                }
                val date = video.videoSnippet.publishedAt.substring(8,10)
                val month = video.videoSnippet.publishedAt.substring(5,7)
                val year = video.videoSnippet.publishedAt.substring(0,4)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${if(date.substring(0,1) == "0") date.substring(1,2) else date} thg ${if(month.substring(0,1) == "0") month.substring(1,2) else month}",
                        style = upperTextStyle
                    )
                    Text(text = year)
                }
            }
            Card(
            ) {
                Text(
                    text = video.videoSnippet.description,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun CommentBottomSheet(commentList: YoutubeVideoComment?) {
    Column {
        Text(
            text = "Bình luận",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.padding(8.dp)
        )
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(commentList!!.items) { commentItem ->
                Row(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = commentItem.snippet.topLevelComment.snippet.authorProfileImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .size(20.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text(
                            text = "${commentItem.snippet.topLevelComment.snippet.authorDisplayName} · ${calculateTime(commentItem.snippet.topLevelComment.snippet.updatedAt)} ${if(commentItem.snippet.topLevelComment.snippet.publishedAt != commentItem.snippet.topLevelComment.snippet.updatedAt) "(đã chỉnh sửa)" else ""}",
                            style = TextStyle(color = Color.DarkGray))
                        Text(
                            text = commentItem.snippet.topLevelComment.snippet.textOriginal,
                            style = TextStyle(color = Color.Black, fontSize = 15.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(50.dp)
                .height(5.dp),
        ){}
    }
}














val thumbnails = Thumbnails()

val localized = Localized(
    title = "Google I/O 101: Q&A On Using Google APIs",
    description = "Antonio Fuentes speaks to us and takes questions on working with Google APIs and OAuth 2.0."
)

val snippet = VideoSnippet(
    publishedAt = "2012-06-20T23:12:38Z",
    channelId = "UC_x5XG1OV2P6uZZ5FSM9Ttw",
    title = "Google I/O 101: Q&A On Using Google APIs Quốc hội sẽ phê chuẩn miễn nhiệm bộ trưởng Bộ Công an đối với đại tướng Tô Lâm\n",
    description = "Antonio Fuentes speaks to us and takes questions on working with Google APIs and OAuth 2.0.",
    thumbnails = thumbnails,
    channelTitle = "Google for Developers",
    liveBroadcastContent = "none",
    publishTime = ""
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
    searchResponseId = SearchResponseId(kind = "", id = "7lCDEYXw3mM"),
    videoSnippet = snippet,
    statistics = statistics,
    channel = mockChannel
)

val pageInfo = PageInfo(
    totalResults = 1,
    resultsPerPage = 1
)
val mockResponse = YoutubeVideoComment(
    kind = "youtube#commentThreadListResponse",
    etag = "AJu8P3igGW7jVKpKpKixMKQsbnE",
    nextPageToken = "Z2V0X25ld2VzdF9maXJzdC0tQ2dnSWdBUVZGN2ZST0JJRkNJY2dHQUFTQlFpb0lCZ0FFZ1VJaVNBWUFCSUZDSjBnR0FFU0JRaUlJQmdBR0FBaURRb0xDUC03aXE0R0VLRENnQW8=",
    pageInfo = PageInfo(
        totalResults = 1,
        resultsPerPage = 1
    ),
    items = listOf(
        CommentItem(
            kind = "youtube#commentThread",
            etag = "PcNkxf05iZhB3fLY-E7M50zJkmc",
            id = "UgxKMpi4Lh2-UR2XH5t4AaABAg",
            snippet = CommentSnippet(
                channelId = "UCaizTs-t-jXjj8H0-S3ATYA",
                videoId = "SIm2W9TtzR0",
                topLevelComment = TopLevelComment(
                    kind = "youtube#comment",
                    etag = "xmeFFiGt2P3-Y4QYZLWlcCE-v8Q",
                    id = "UgxKMpi4Lh2-UR2XH5t4AaABAg",
                    snippet = TopLevelCommentSnippet(
                        channelId = "UCaizTs-t-jXjj8H0-S3ATYA",
                        videoId = "SIm2W9TtzR0",
                        textDisplay = "Updated Video <a href=\"https://www.youtube.com/watch?v=A1III_DQU4I\">https://youtu.be/A1III_DQU4I?si=_8-d4OpoIHpr2jJm</a>",
                        textOriginal = "Updated Video https://youtu.be/A1III_DQU4I?si=_8-d4OpoIHpr2jJm",
                        authorDisplayName = "@analyticswithadam",
                        authorProfileImageUrl = "https://yt3.ggpht.com/La5UQrwbtvM0aYMi95LUJZRX9maQUJeYm49VffEU3xSYxr-sEFLdOUqQI71UPNHBb2Ye7xNF7g=s48-c-k-c0x00ffffff-no-rj",
                        authorChannelUrl = "http://www.youtube.com/@analyticswithadam",
                        authorChannelId = AuthorChannelId(
                            value = "UCaizTs-t-jXjj8H0-S3ATYA"
                        ),
                        canRate = true,
                        viewerRating = "none",
                        likeCount = 1,
                        publishedAt = "2024-02-06T21:00:47Z",
                        updatedAt = "2024-02-06T21:00:47Z"
                    )
                ),
                canReply = true,
                totalReplyCount = 0,
                isPublic = true
            )
        )
    )
)

// Finally, create the YoutubeVideo object
val fakeVideo = YoutubeVideo(
    etag = "",
    kind = "",
    items = listOf(videoItem),
    pageInfo = pageInfo,
    regionCode = "",
    nextPageToken = ""
)


@Preview(showBackground = true)
@Composable
private fun WatchVideoScreenCommentPreview() {
    CommentBottomSheet(commentList = mockResponse)
}

@Preview(showBackground = true)
@Composable
private fun WatchVideoScreenDescriptionPreview() {
    DecriptionBottomSheet(video = fakeVideo.items[0])
}

@Preview(showBackground = true)
@Composable
private fun WatchVideoScreenPreview() {

    WatchVideo(video = fakeVideo.items[0], commentList = mockResponse, modifier = Modifier, { }, { }, Modifier)
}
