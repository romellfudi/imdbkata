package com.example.home.data.firebase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseHome @Inject constructor(
    private val auth: FirebaseAuth,
) {

    fun signOut(): Boolean {
        return try {
            auth.signOut()
            true
        } catch (e: Exception) {
            false
        }
    }
}
