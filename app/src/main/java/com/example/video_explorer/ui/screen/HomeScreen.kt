package com.example.video_explorer.ui.screen

import com.example.video_explorer.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.video_explorer.data.YoutubeVideoRepository
import com.example.video_explorer.data.state.HomeScreenUiState
import com.example.video_explorer.data.state.SignInUiState
import com.example.video_explorer.data.youtubeData.ChannelItem
import com.example.video_explorer.data.youtubeData.SearchResponseId
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.parts.ChannelSnippet
import com.example.video_explorer.data.youtubeData.parts.ChannelStatistics
import com.example.video_explorer.data.youtubeData.parts.Default
import com.example.video_explorer.data.youtubeData.parts.Localized
import com.example.video_explorer.data.youtubeData.parts.PageInfo
import com.example.video_explorer.data.youtubeData.parts.VideoSnippet
import com.example.video_explorer.data.youtubeData.parts.Thumbnails
import com.example.video_explorer.data.youtubeData.parts.TopicDetails
import com.example.video_explorer.data.youtubeData.parts.VideoStatistics
import com.example.video_explorer.model.YoutubeViewModel
import com.example.video_explorer.ui.Screen
import com.example.video_explorer.ui.render.calculateTime
import com.example.video_explorer.ui.render.calculateView
import com.example.video_explorer.ui.render.reduceStringLength

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    youtubeViewModel: YoutubeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Log.i("ex_mess", "HomeScreen Run Start")
    val homeScreenUiState = youtubeViewModel.homeScreenUiState
    when(homeScreenUiState) {
        is HomeScreenUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
            Log.i("ex_mess", "HomeScreen Is Loading")
        }
        is HomeScreenUiState.Success -> {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    YoutubeTopAppBar(
                        signInUiState = youtubeViewModel.signInUiState,
                        scrollBehavior = scrollBehavior,
                        searchHandler = {searchString ->
                            youtubeViewModel.getHomeVideoList(searchString = searchString)
                        },
                        onProfileClick = {
                            navController.navigate(route = Screen.ProfileScreen.name)
                        }

                    )
                }
            ) { innerPadding ->
                HomeScreenList(
                    homeScreenUiState = homeScreenUiState,
                    youtubeViewModel = youtubeViewModel,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        is HomeScreenUiState.Error -> {
//            Log.i("ex_mess", "HomeScreen Is Error")
            ErrorScreen(errorNote = homeScreenUiState.errorNote)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeTopAppBar(
    signInUiState: SignInUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    searchHandler: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchString by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearching){
        if (isSearching)
            focusRequester.requestFocus()
        if (isFocused == false)
            isSearching = false
    }
    TopAppBar(
        modifier = modifier
            .padding(end = 16.dp),
        scrollBehavior = scrollBehavior,
        title = {
            if(isSearching == false) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(32.dp))
                    Text(text = "Youtube")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon",
                            tint = Color.Black,
                            modifier = Modifier
//                                .padding(end = 8.dp)
                                .size(36.dp)
                                .clickable {
                                    isSearching = true
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val profilePicModifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { onProfileClick() }
                        when(signInUiState) {
                            is SignInUiState.SignedIn -> {
                                if(signInUiState.user != null) {
                                    AsyncImage(
                                        model = signInUiState.user.profilePictureUrl,
                                        contentDescription = "Profile picture",
                                        modifier = profilePicModifier,
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.profile),
                                        contentDescription = "Profile picture",
                                        modifier = profilePicModifier,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            is SignInUiState.NotSignedIn -> {
                                Image(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = "Profile picture",
                                    modifier = profilePicModifier,
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }

                    }
                }
            } else {
                Row {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Search Icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(36.dp)
                            .clickable {
                                isSearching = false
                            }
                    )
                    SearchBar(
                        query = searchString,
                        onQueryChange = { searchString = it },
                        onFocusChange = { focusState: FocusState ->
                            isFocused = focusState.isFocused
                        },
                        focusRequester = focusRequester,
                        searchClick = {
                            isFocused = false
                            searchHandler(searchString)
                        }
                    )
                }
            }
        }
    )
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    focusRequester: FocusRequester,
    searchClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
) {
    Box(
        modifier = modifier
            .background(Color.LightGray, MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding()
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { searchClick() }
                ),
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp
                ),
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(color = Color.LightGray, fontSize = 18.sp)
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        onFocusChange(focusState)
                    }
            )
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
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items((homeScreenUiState as HomeScreenUiState.Success).videoList.items) { videoItem ->
            VideoItem(
                video = videoItem,
                onClick = {
                    navController.navigate(Screen.WatchVideo.name)
                    youtubeViewModel.setWatchScreenUiStateToSuccess(videoItem)
                }
            )
        }
    }
}


@Composable
fun VideoItem(video: VideoItem, onClick: () -> Unit, modifier: Modifier= Modifier) {
    val channel = video.channel
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(onClick = onClick)

    ) {
        Image(
            painter = rememberAsyncImagePainter(model = video.videoSnippet.thumbnails.medium.url),
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
            if (channel != null)
                Image(
                    painter = rememberAsyncImagePainter(model = channel.snippet.thumbnails.default.url),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            else
                Image(
                    painter = painterResource(id = R.drawable.thumb_down),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = reduceStringLength(video.videoSnippet.title, 70),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
                if(video.statistics != null)
                    Text(text = "${video.videoSnippet.channelTitle} · ${calculateView(video.statistics!!.viewCount)} lượt xem · ${calculateTime(video.videoSnippet.publishedAt)}")
                else
                    Text(text = "${video.videoSnippet.channelTitle} · ${calculateTime(video.videoSnippet.publishedAt)}")
            }
        }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {

    YoutubeTopAppBar(signInUiState = SignInUiState.NotSignedIn(), scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(), {}, onProfileClick = {})
//    SearchBar(
//        query = query,
//        onQueryChange = { query = it },
//        modifier = Modifier.padding(16.dp)
//    )
}

@Preview(showBackground = true)
@Composable
fun VideoItemPreview() {
    val thumbnails = Thumbnails()

    Localized(
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
        searchResponseId = SearchResponseId(kind = "", id = "MV8moKp1Wxw"),
        videoSnippet = snippet,
        statistics = statistics,
        channel = mockChannel
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
        pageInfo = pageInfo,
        regionCode = "",
        nextPageToken = ""
    )

    VideoItem(video = fakeVideo.items[0], onClick = { /*TODO*/ })
}
