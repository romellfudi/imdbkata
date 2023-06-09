package com.example.login.data.firebase

import com.example.login.helpers.LoginState
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class FirebaseLogin @Inject constructor(
    private val auth: FirebaseAuth,
) {

    /**
     * Try to log in with anonymous account
     */
    suspend fun logInAnonymously(): LoginState {
        return try {
            auth.signInAnonymously().await()
            LoginState.Success()
        } catch (e: Exception) {
            LoginState.Error(e.message ?: "ERROR")
        }
    }

    /**
     * Try to log in with email and password
     */
    suspend fun logInWithEmailAndPassword(email: String, password: String): LoginState {
        return try {
            auth.signInWithEmailAndPassword(email, password)
                .await()
            LoginState.Success(auth.currentUser?.email ?: "")
        } catch (e: Exception) {
            LoginState.Error(e.message ?: "ERROR")
        }
    }

    /**
     * Try to log in with Google account
     */
    suspend fun logInWithGoogle(googleCredential: AuthCredential): LoginState {
        return try {
            auth.signInWithCredential(googleCredential).await()
            val displayedName = auth.currentUser?.displayName ?: ""
            LoginState.Success(displayedName)
        } catch (e: Exception) {
            LoginState.Error(e.message ?: "ERROR")
        }
    }

    /**
     * Try to log in with Facebook account
     */
    suspend fun logInWithFacebook(accessToken: AccessToken): LoginState {
        val facebookCredential = FacebookAuthProvider.getCredential(accessToken.token)
        return try {
            auth.signInWithCredential(facebookCredential).await()
            val displayedName = auth.currentUser?.displayName ?: ""
            LoginState.Success(displayedName)
        } catch (e: Exception) {
            LoginState.Error(e.message ?: "ERROR")
        }
    }
}
