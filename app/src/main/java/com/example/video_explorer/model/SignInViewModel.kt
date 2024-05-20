package com.example.video_explorer.model

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.content.Context
import com.example.video_explorer.data.state.SignInUiState
import kotlinx.coroutines.flow.update

class SignInViewModel (

): ViewModel() {
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

//    private val _password = MutableStateFlow("")
//    val password = _password.asStateFlow()
    var password by mutableStateOf("")
        private set

//    private val _signInUiState = MutableStateFlow(SignInUiState())
//    val signInUiState = _signInUiState.asStateFlow()
//
//
//    fun onSignInResult(result: SignInResult) {
//        _signInUiState.update { it.copy(
//            isLoginSuccessful = result.data != null,
//            signInError = result.errorMessage
//        ) }
//    }
//
//    fun resetSignInUiState() {
//        _signInUiState.update { SignInUiState() }
//    }

    fun updateUsername(newValue: String) {
        _username.value = newValue
    }
//    fun updatePassword(newValue: String) {
//        _password.value = newValue
//    }

    fun updatePassword(newValue: String) {
        password = newValue
    }

    fun showToast(value: String = "", context: Context) {
        Toast.makeText(context, "${value} ${username.value} ${password}", Toast.LENGTH_SHORT).show()
    }
}