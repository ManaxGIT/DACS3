package com.example.video_explorer.data.state

import com.example.video_explorer.data.user.UserData

sealed interface SignInUiState {
    data class SignedIn(val user: UserData?) : SignInUiState
    data class NotSignedIn(val errorMessage: String? = null): SignInUiState
}