package com.example.login.usecase

import com.example.login.data.LoginRepository
import com.facebook.AccessToken
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to sign in with Facebook
 */
class SignInFacebookUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Invoke the use case
     * @param accessToken Facebook access token
     * @return Flow of [LoginState]
     */
    operator fun invoke(accessToken: AccessToken) = flow {
        emit(loginRepository.signInWithFacebook(accessToken))
    }

}
