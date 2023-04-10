package com.example.login.data

import com.example.login.data.firebase.FirebaseLogin
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val firebaseLogin: FirebaseLogin
) {
    suspend fun signInAnonymously() = withContext(Dispatchers.IO) {
        firebaseLogin.logInAnonymously()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) =
        withContext(Dispatchers.IO) {
            firebaseLogin.logInWithEmailAndPassword(email, password)
        }

    suspend fun signInWithGoogle(credential: AuthCredential) =
        withContext(Dispatchers.IO) {
            firebaseLogin.logInWithGoogle(credential)
        }

    suspend fun signInWithFacebook(accessToken: AccessToken) =
        withContext(Dispatchers.IO) {
            firebaseLogin.logInWithFacebook(accessToken)
        }
}
