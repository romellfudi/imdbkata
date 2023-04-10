package com.example.login.data.firebase

import com.example.login.helpers.LoginState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignup @Inject constructor(
    private val auth: FirebaseAuth,
) {

    suspend fun signup(username: String, emailAddress: String, userPassword: String): LoginState {
        return try {
            auth.createUserWithEmailAndPassword(emailAddress, userPassword).await()
            LoginState.Success(auth.currentUser?.email?: "")
        } catch (e: Exception) {
            LoginState.Error(e.message ?: "ERROR")
        }
    }
}
