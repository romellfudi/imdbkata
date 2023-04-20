package com.example.home.usecase.firebase

import com.example.home.data.FirebaseUserRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFirebaseUserUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    operator fun invoke() = flow {
        emit(firebaseUserRepository.getUserLogged())
    }
}
