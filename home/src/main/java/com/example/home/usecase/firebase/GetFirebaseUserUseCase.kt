package com.example.home.usecase.firebase

import com.example.home.data.FirebaseUserRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to get the user logged in Firebase
 */
class GetFirebaseUserUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    /**
     * Invoke the use case to get the user logged in Firebase
     * @return [FirebaseUser] the user logged in Firebase
     */
    operator fun invoke() = flow {
        emit(firebaseUserRepository.getUserLogged())
    }
}
