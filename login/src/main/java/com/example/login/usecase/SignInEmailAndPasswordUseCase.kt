package com.example.login.usecase

import com.example.login.data.LoginRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInEmailAndPasswordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(email: String, password: String) = flow {
        emit(loginRepository.signInWithEmailAndPassword(email, password))
    }

}
