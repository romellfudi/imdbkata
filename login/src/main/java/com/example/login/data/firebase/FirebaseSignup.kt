package com.example.login.data.firebase

import com.example.login.helpers.LoginState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignup @Inject constructor(
    private val auth: FirebaseAuth,
) {

    suspend fun signup(username: String, emailAddress: String, userPassword: String): LoginState {
        return try {
            auth.createUserWithEmailAndPassword(emailAddress, userPassword).await()
            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
//                    .setPhotoUri()
                    .build()
            )
            LoginState.Success(auth.currentUser?.email?: "")
        } catch (e: Exception) {
            LoginState.Error(e.message ?: "ERROR")
        }
    }
}
