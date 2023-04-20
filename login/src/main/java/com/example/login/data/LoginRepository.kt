package com.example.login.data

import com.example.login.data.firebase.FirebaseLogin
import com.example.login.helpers.LoginState
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository module for handling data operations.
 */
class LoginRepository @Inject constructor(
    private val firebaseLogin: FirebaseLogin
) {

    /**
     * Log in with anonymous account.
     * @return [LoginState] object.
     */
    suspend fun signInAnonymously() = withContext(Dispatchers.IO) {
        firebaseLogin.logInAnonymously()
    }

    /**
     * Log in with email and password.
     * @param email Email.
     * @param password Password.
     * @return [LoginState] object.
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String) =
        withContext(Dispatchers.IO) {
            firebaseLogin.logInWithEmailAndPassword(email, password)
        }

    /**
     * Log in with Google account.
     * @param credential Google credential.
     * @return [LoginState] object.
     */
    suspend fun signInWithGoogle(credential: AuthCredential) =
        withContext(Dispatchers.IO) {
            firebaseLogin.logInWithGoogle(credential)
        }

    /**
     * Log in with Facebook account.
     * @param accessToken Facebook access token.
     * @return [LoginState] object.
     */
    suspend fun signInWithFacebook(accessToken: AccessToken) =
        withContext(Dispatchers.IO) {
            firebaseLogin.logInWithFacebook(accessToken)
        }
}
