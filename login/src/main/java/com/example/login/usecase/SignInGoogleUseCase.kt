package com.example.login.usecase

import com.example.login.data.LoginRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to sign in with google
 */
class SignInGoogleUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Invoke the use case
     * @param authCredential the credential to sign in
     * @return [LoginState] the result of the sign in
     */
    operator fun invoke(authCredential: AuthCredential) = flow {
        emit(loginRepository.signInWithGoogle(authCredential))
    }

}
