package com.example.video_explorer.ui
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.data.SourceContext
import androidx.navigation.NavHostController
import com.example.video_explorer.data.state.WatchVideoUiState
import com.example.video_explorer.model.GoogleAuthUiClient
import com.example.video_explorer.model.SignInViewModel
import com.example.video_explorer.model.YoutubeViewModel
import com.example.video_explorer.ui.screen.HomeScreen
import com.example.video_explorer.ui.screen.WatchVideoScreen
import com.example.video_explorer.ui.screen.user.LoginScreen
import com.example.video_explorer.ui.screen.user.ProfileScreen
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class Screen(val title: String) {
    HomeScreen(title = "Youtube"),
    WatchVideo(title = ""),
    LoginScreen(title = ""),
    ProfileScreen(title = "")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoExplorerApp(
    applicationContext: Context,
    RESULT_OK: Int,
    navController: NavHostController = rememberNavController(),
    youtubeViewModel: YoutubeViewModel = viewModel(factory = YoutubeViewModel.Factory)
) {
    Log.i("ex_mess", "Video Explorer App Run")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { YoutubeTopAppBar(
            scrollBehavior = scrollBehavior,
            onProfileClick = {
                navController.navigate(Screen.ProfileScreen.name)
            }
        ) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Screen.HomeScreen.name) {
                    HomeScreen(
                        watchVideoUiState = youtubeViewModel.watchVideoUiState,
                        homeScreenUiState = youtubeViewModel.homeScreenUiState,
                        navController = navController
                    )
                }
                composable(route = Screen.WatchVideo.name) {
                    WatchVideoScreen((youtubeViewModel.watchVideoUiState as WatchVideoUiState.Success).youtubeVideoItem)
                }
                composable(route = Screen.LoginScreen.name) {
                    LoginScreen(loginViewModel = SignInViewModel())
                }
                composable(route = Screen.ProfileScreen.name) {
                    LaunchedEffect(key1 = Unit) {
                        val currentSignedInUser = googleAuthUiClient.getSignedInUser()
                        if(currentSignedInUser != null) {
//                            googleAuthUiClient.signOut()
//                            youtubeViewModel.resetSignInUiState()
                            youtubeViewModel.setSignInUiState(currentSignedInUser)
//                            navController.navigate(route = Screen.ProfileScreen.name)
                        }
                    }
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if(result.resultCode == RESULT_OK) {
                                scope.launch {
                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = result.data ?: return@launch
                                    )
                                    youtubeViewModel.onSignInResult(signInResult)
                                }
                            }
                        }
                    )
                    ProfileScreen(
                        signInUiState = youtubeViewModel.signInUiState,
                        onSignIn = {
                            Log.i("ex_mess", "Begin sign in")
                            scope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                            Log.i("ex_mess", "End sign in")
                        },
                        onSignOut = {
                            scope.launch {
                                Log.i("ex_mess", "Begin sign Out")
                                googleAuthUiClient.signOut()
                                youtubeViewModel.resetSignInUiState()
                                Toast.makeText(
                                    applicationContext,
                                    "Signed out",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.popBackStack()
                                Log.i("ex_mess", "End sign out")
                            }
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row {
                Text(
                    text = "Chau Van Man",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        },
        modifier = modifier
            .clickable(onClick = onProfileClick)
    )
}




