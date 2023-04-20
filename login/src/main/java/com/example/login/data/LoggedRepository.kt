package com.example.login.data

import com.example.login.data.firebase.FirebaseLogged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository to check if user is logged
 */
class LoggedRepository @Inject constructor(
    private val firebaseLogged: FirebaseLogged,
) {

    /**
     * Check if user is logged
     */
    suspend fun isUserLogged() = withContext(Dispatchers.Default) {
        firebaseLogged.isUserLogged()
    }

}
