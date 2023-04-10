package com.example.login.data

import com.example.login.data.firebase.FirebaseLogged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoggedRepository @Inject constructor(
    private val firebaseLogged: FirebaseLogged,
) {

    suspend fun isUserLogged() = withContext(Dispatchers.Default) {
        firebaseLogged.isUserLogged()
    }

}
