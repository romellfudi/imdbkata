package com.example.login.usecase

import com.example.login.data.SignupRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    operator fun invoke(name: String, email: String, password: String) = flow {
        emit(signupRepository.signup(name, email, password))
    }

}