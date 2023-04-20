package com.example.login.data

import com.example.login.data.firebase.FirebaseSignup
import com.example.login.helpers.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * This class is used to sign up a user
 */
class SignupRepository @Inject constructor(
    private val firebaseSignup: FirebaseSignup,
) {

    /**
     * This function is used to sign up a user
     * @param name: String
     * @param email: String
     * @param password: String
     * @return LoginState
     */
    suspend fun signup(name: String, email: String, password: String) =
        withContext(Dispatchers.Default) {
            firebaseSignup.signup(name, email, password)
        }
}
