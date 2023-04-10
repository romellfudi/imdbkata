package com.example.login.usecase

import com.example.login.data.LoginRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInGoogleUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(authCredential: AuthCredential) = flow {
        emit(loginRepository.signInWithGoogle(authCredential))
    }

}
