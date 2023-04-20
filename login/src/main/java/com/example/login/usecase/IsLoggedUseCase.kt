package com.example.login.usecase

import com.example.login.data.LoggedRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to check if the user is logged
 */
class IsLoggedUseCase @Inject constructor(
    private val loggedRepository: LoggedRepository
) {
    /**
     * invoke the use case to check if the user is logged
     * @return [LoggedState] true if the user is logged, false otherwise
     */
    operator fun invoke() = flow {
        emit(loggedRepository.isUserLogged())
    }
}
