package com.example.login.usecase

import com.example.login.data.LoginRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to sign in anonymously
 */
class SignInAnonymouslyUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Invoke the use case to sign in anonymously
     * @return [Flow] of [LoginState] to indicate if the sign in was successful
     */
    operator fun invoke() = flow {
        emit(loginRepository.signInAnonymously())
    }

}
