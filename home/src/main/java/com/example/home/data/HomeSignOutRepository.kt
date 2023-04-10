package com.example.home.data

import com.example.home.data.firebase.FirebaseHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeSignOutRepository @Inject constructor(
    private val firebase: FirebaseHome
) {

    suspend fun signOut() = withContext(Dispatchers.Default) {
            firebase.signOut()
        }

}
