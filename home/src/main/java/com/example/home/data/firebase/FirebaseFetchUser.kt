package com.example.home.data.firebase

import com.example.data.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseFetchUser @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun getUserProfile(): User? = withContext(Dispatchers.IO) {
        auth.currentUser?.run {
            User(
                displayName ?: "",
                email ?: "",
                photoUrl.toString(),
                if (providerId.contains("facebook")) "facebook" else "google",
            )
        }
    }

}
