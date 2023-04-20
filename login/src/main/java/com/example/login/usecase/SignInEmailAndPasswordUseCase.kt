package com.example.login.usecase

import com.example.login.data.LoginRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to sign in with email and password
 */
class SignInEmailAndPasswordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Invoke the use case to sign in with email and password
     * @param email email to sign in
     * @param password password to sign in
     * @return [Flow] of [LoginState] to indicate if the sign in was successful
     */
    operator fun invoke(email: String, password: String) = flow {
        emit(loginRepository.signInWithEmailAndPassword(email, password))
    }

}
