package com.example.home.usecase.firebase

import com.example.home.data.HomeSignOutRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to sign out from firebase
 */
class SignOutUseCase @Inject constructor(
    private val homeSignOutRepository: HomeSignOutRepository
) {
    /**
     * Invoke the use case to sign out from firebase+
     * @return a flow of [Boolean]
     */
    operator fun invoke() = flow {
        emit(homeSignOutRepository.signOut())
    }
}
