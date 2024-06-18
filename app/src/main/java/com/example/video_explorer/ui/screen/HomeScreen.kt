package com.example.video_explorer.ui.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import com.example.video_explorer.R
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
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
import com.example.video_explorer.ui.render.convertDuration
import com.example.video_explorer.ui.render.reduceStringLength

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    youtubeViewModel: YoutubeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Log.i("ex_mess", "HomeScreen Run Start")


    var searchString: String by remember { mutableStateOf("lap trinh") }
    var orderCriteria: String by remember { mutableStateOf("Mức độ liên quan") }
    var typeCriteria: String by remember { mutableStateOf("Video") }
    var dateCriteria: String by remember { mutableStateOf("Mọi thời điểm") }
    var lengthCriteria: String by remember { mutableStateOf("Bất kỳ") }

    val context = LocalContext.current

    (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                        searchHandler = {searchString, order, type, date, length ->
                            youtubeViewModel.getHomeVideoList(searchString = searchString, order = order, type = type, date = date, length = length)
                        },
                        onProfileClick = {
                            navController.navigate(route = Screen.ProfileScreen.name)
                        },
                        searchString = searchString,
                        orderCriteria = orderCriteria,
                        typeCriteria = typeCriteria,
                        dateCriteria = dateCriteria,
                        lengthCriteria = lengthCriteria,
                        onSearchStringChange = {newSearchString ->
                            searchString = newSearchString
                        },
                        onOrderChange = {menuBoxValue ->
                            orderCriteria = menuBoxValue
                        },
                        onTypeChange = {menuBoxValue ->
                            typeCriteria = menuBoxValue
                        },
                        onDateChange = {menuBoxValue ->
                            dateCriteria = menuBoxValue
                        },
                        onLengthChange = {menuBoxValue ->
                            lengthCriteria = menuBoxValue
                        },

                    )
                }
            ) { innerPadding ->
                Column {
                    HomeScreenList(
                        homeScreenUiState = homeScreenUiState,
                        youtubeViewModel = youtubeViewModel,
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onExpandClick = {
                            val originYoutubeVideoList: YoutubeVideo = homeScreenUiState.videoList
                            youtubeViewModel.expandVideoList(originYoutubeVideoList = originYoutubeVideoList, searchString = searchString, order = orderCriteria, type = typeCriteria, date = dateCriteria, length = lengthCriteria, nextPageToken = originYoutubeVideoList.nextPageToken)
                        }
                    )
                }
            }
        }
        is HomeScreenUiState.Error -> {
//            Log.i("ex_mess", "HomeScreen Is Error")
            ErrorScreen(errorNote = homeScreenUiState.errorNote)
        }
    }
}

@Composable
fun ExpandBar(
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onExpandClick() },
        contentAlignment = Alignment.Center,

    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Drop down arrow",
            tint = Color.Gray,
            modifier = Modifier
                .size(40.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeTopAppBar(
    signInUiState: SignInUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    searchHandler: (String,String?,String?,String?,String?) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    searchString: String,
    onSearchStringChange: (String) -> Unit,
    orderCriteria: String,
    onOrderChange: (String) -> Unit,
    typeCriteria: String,
    onTypeChange: (String) -> Unit,
    dateCriteria: String,
    onDateChange: (String) -> Unit,
    lengthCriteria: String,
    onLengthChange: (String) -> Unit,
) {
    var isSearching by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var isConditionBoxShow by remember { mutableStateOf(false) }

    LaunchedEffect(isSearching){
        if (isSearching == true)
            focusRequester.requestFocus()
        if (isFocused == false)
            isSearching = false
    }
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.LightGray),
        title = {
            if(isSearching == false) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.youtube_logo),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .height(36.dp),
//                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Youtube",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                        modifier = Modifier)
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
                            .padding(end = 4.dp)
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
                        onQueryChange = { onSearchStringChange(it) },
                        onFocusChange = { focusState: FocusState ->
                            isFocused = focusState.isFocused
                        },
                        focusRequester = focusRequester,
                        searchClick = {
                            isFocused = false
                            searchHandler(searchString, orderCriteria, null, dateCriteria, lengthCriteria)
                        }
                    )

                }
            }

        },
        actions = {
            if(isSearching)
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Search Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable {
                            isConditionBoxShow = !isConditionBoxShow
                        }
                )
            if(isConditionBoxShow) {
                CustomDialog(
                    onConfirm = {
                        searchHandler(searchString, orderCriteria, typeCriteria, dateCriteria, lengthCriteria)
                    },
                    onDismiss = {
                        isConditionBoxShow = false
                    },
                    orderCriteria = orderCriteria,
                    onOrderChange = {menuBoxValue: String ->
                        onOrderChange(menuBoxValue)
                    },
                    typeCriteria = typeCriteria,
                    onTypeChange = {menuBoxValue: String ->
                        onTypeChange(menuBoxValue)
                    },
                    dateCriteria = dateCriteria,
                    onDateChange = {menuBoxValue: String ->
                        onDateChange(menuBoxValue)
                    },
                    lengthCriteria = lengthCriteria,
                    onLengthChange = {menuBoxValue: String ->
                        onLengthChange(menuBoxValue)
                    },
                )
            }
        }
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    orderCriteria: String,
    onOrderChange: (String) -> Unit,
    typeCriteria: String,
    onTypeChange: (String) -> Unit,
    dateCriteria: String,
    onDateChange: (String) -> Unit,
    lengthCriteria: String,
    onLengthChange: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        @Composable
        fun InnerItems(
            labelName: String,
            value: String,
            onValueChange: (String) -> Unit,
            choiceList: List<String>,
        ) {
            var isExpanded: Boolean by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = labelName,
                    style = TextStyle(fontSize = 18.sp),
                    modifier = Modifier
                        .padding(8.dp)
                        .width(90.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = {isExpanded = it}
                ) {
                    TextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        choiceList.forEach{ choice->
                            DropdownMenuItem(
                                text = { Text(text = choice) },
                                onClick = {
                                    onValueChange(choice)
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .width(300.dp)
//                .height(200.dp)
                .background(Color.White)
        ){
            Text(
                text = "Bộ lọc tìm kiếm",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                modifier = Modifier.padding(20.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                InnerItems(
                    labelName = "Sắp xếp theo",
                    value = orderCriteria,
                    onValueChange = { menuBoxValue: String ->
                        onOrderChange(menuBoxValue)
                    },
                    choiceList = listOf("Mức độ liên quan", "Ngày tải lên", "Lượt xem", "Xếp hạng")
                )
                InnerItems(
                    labelName = "Loại",
                    value = typeCriteria,
                    onValueChange = { menuBoxValue: String ->
                        onTypeChange(menuBoxValue)
                    },
                    choiceList = listOf("Video")
                )
                InnerItems(
                    labelName = "Ngày tải lên",
                    value = dateCriteria,
                    onValueChange = { menuBoxValue: String ->
                        onDateChange(menuBoxValue)
                    },
                    listOf("Mọi thời điểm", "Một giờ qua", "Hôm nay", "Tuần này", "Tháng này", "Năm nay")
                )
                InnerItems(
                    labelName = "Thời lượng",
                    value = lengthCriteria,
                    onValueChange = { menuBoxValue: String ->
                        onLengthChange(menuBoxValue)
                    },
                    choiceList = listOf("Bất kỳ", "Dưới 4 phút", "4 - 20 phút", "Trên 20 phút")
                )
            }
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ){
                Text(
                    text = "Huỷ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xff0073e6)),
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            onOrderChange("Mức độ liên quan")
                            onTypeChange("Video")
                            onDateChange("Mọi thời điểm")
                            onLengthChange("Bất kỳ")
                            onDismiss()
                        }
                )
                Text(
                    text = "Áp dụng",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xff0073e6)),
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            onConfirm()
                        }
                )
            }
        }
    }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeScreenList(
    homeScreenUiState: HomeScreenUiState,
    youtubeViewModel: YoutubeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onExpandClick: () -> Unit
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
        item {
            ExpandBar(onExpandClick = { onExpandClick() })
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
        Box{
            AsyncImage(
                model = video.videoSnippet.thumbnails.high.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ){
                Card(
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xB3000000)),
                    modifier = Modifier.padding(top = 182.dp, end = 8.dp)
                ){
                    Text(
                        text = convertDuration(video.duration),
                        modifier = Modifier.padding(4.dp),
                        style = TextStyle(color = Color.White)
                    )
                }
            }
        }
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
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp)
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

    YoutubeTopAppBar(signInUiState = SignInUiState.NotSignedIn(), scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(), {a,b,c,d,e->

    }, onProfileClick = {}, Modifier, "", {test -> },"", {test -> },"", {test -> },"", {test -> },"", {test -> },)
//    SearchBar(
//        query = query,
//        onQueryChange = { query = it },
//        modifier = Modifier.padding(16.dp)
//    )
}

@Preview(showBackground = true)
@Composable
fun CustomDialogPreview() {
    CustomDialog({}, {}, "",{},"",{},"",{},"",{},)
}

@RequiresApi(Build.VERSION_CODES.O)
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
        channel = mockChannel,
        duration = "PT45H12M0S"
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
