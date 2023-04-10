package com.example.home.usecase

import com.example.home.data.HomeSignOutRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val homeSignOutRepository: HomeSignOutRepository
) {
    operator fun invoke() = flow {
        emit(homeSignOutRepository.signOut())
    }
}
