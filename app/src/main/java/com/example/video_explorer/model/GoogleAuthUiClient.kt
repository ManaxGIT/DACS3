package com.example.video_explorer.model

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.video_explorer.R
import com.example.video_explorer.data.state.SignInUiState
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException
import com.example.video_explorer.data.user.UserData
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            Log.i("ex_mess2", e.toString())
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInUiState {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInUiState.SignedIn(
                user = user?.let {
                    UserData(
                        userId = it.uid,
                        username = user.displayName,
                        profilePictureUrl = user.photoUrl?.toString()
                    )
                }
            )
        } catch(e: Exception) {
            Log.i("ex_mess1", e.toString())
            if (e is CancellationException) throw e
            SignInUiState.NotSignedIn(
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            Log.i("ex_mess", e.toString())
            if (e is CancellationException) throw e
        }

    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        Log.i("ex_user", uid)
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            ).setAutoSelectEnabled(false)
            .build()
    }
}