package com.example.home.usecase.firebase

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFirebaseUserUseCase @Inject constructor(
    private val firebaseUserRepository: com.example.home.data.FirebaseUserRepository
) {
    operator fun invoke() = flow {
        emit(firebaseUserRepository.getUserLogged())
    }
}
