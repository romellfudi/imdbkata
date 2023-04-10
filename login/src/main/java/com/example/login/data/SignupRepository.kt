package com.example.login.data

import com.example.login.data.firebase.FirebaseSignup
import com.example.login.helpers.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignupRepository @Inject constructor(
    private val firebaseSignup: FirebaseSignup,
) {

    suspend fun signup(name: String, email: String, password: String) =
        withContext(Dispatchers.Default) {
            firebaseSignup.signup(name, email, password)
        }
}
