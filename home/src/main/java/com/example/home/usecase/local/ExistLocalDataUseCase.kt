package com.example.home.usecase.local

import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExistLocalDataUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(): Flow<Boolean> = flow {
        emit(repository.existDataStored())
    }
}
