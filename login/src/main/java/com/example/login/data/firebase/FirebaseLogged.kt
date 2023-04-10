package com.example.login.data.firebase

import com.example.login.helpers.LoggedState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseLogged @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun isUserLogged(): LoggedState =
        withContext(Dispatchers.IO) {
            if (auth.currentUser != null) LoggedState.Logged else LoggedState.NotLogged
        }
}
