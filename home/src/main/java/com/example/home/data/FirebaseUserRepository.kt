package com.example.home.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firebaseFetchUser: com.example.home.data.firebase.FirebaseFetchUser,
) {

    suspend fun getUserLogged() = withContext(Dispatchers.Default) {
        firebaseFetchUser.getUserProfile()
    }

}
