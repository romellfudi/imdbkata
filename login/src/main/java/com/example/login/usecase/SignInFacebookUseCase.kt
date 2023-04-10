package com.example.login.usecase

import com.example.login.data.LoginRepository
import com.facebook.AccessToken
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInFacebookUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(accessToken: AccessToken) = flow {
        emit(loginRepository.signInWithFacebook(accessToken))
    }

}
