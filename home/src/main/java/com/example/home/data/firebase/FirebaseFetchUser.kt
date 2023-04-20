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
                name = displayName ?: "",
                email = if(isAnonymous) "guest" else email ?: "",
                photoUrl = photoUrl.toString(),
                provider = if(providerId.contains("facebook")) "facebook" else "google",
                isAnonymous = isAnonymous
            )
        }
    }

}
