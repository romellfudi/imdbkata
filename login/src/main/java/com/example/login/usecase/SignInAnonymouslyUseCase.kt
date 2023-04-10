package com.example.login.usecase

import com.example.login.data.LoginRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke() = flow {
        emit(loginRepository.signInAnonymously())
    }

}
