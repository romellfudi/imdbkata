package com.example.login.usecase

import com.example.login.data.LoggedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsLoggedUseCase @Inject constructor(
    private val loggedRepository: LoggedRepository
) {
    operator fun invoke() = flow {
        emit(loggedRepository.isUserLogged())
    }
}
