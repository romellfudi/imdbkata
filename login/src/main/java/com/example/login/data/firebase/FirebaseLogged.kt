package com.example.login.data.firebase

import com.example.login.helpers.LoggedState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Class to check if user is logged in
 */
class FirebaseLogged @Inject constructor(
    private val auth: FirebaseAuth
) {
    /**
     * Check if user is logged in
     */
    suspend fun isUserLogged(): LoggedState =
        withContext(Dispatchers.IO) {
            if (auth.currentUser != null) LoggedState.Logged else LoggedState.NotLogged
        }
}
