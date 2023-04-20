package com.example.login.usecase

import com.example.login.data.SignupRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for signing up a user
 */
class SignupUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    /**
     * Signs up a user
     * @param name The name of the user
     * @param email The email of the user
     * @param password The password of the user
     * @return A [flow] of the result [LoginState] of the operation
     */
    operator fun invoke(name: String, email: String, password: String) = flow {
        emit(signupRepository.signup(name, email, password))
    }

}