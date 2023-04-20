package com.example.home.usecase.local

import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to check if there is data stored in local database
 */
class ExistLocalDataUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke use case to check if there is data stored in local database
     * @return [Flow] of [Boolean] with true if there is data stored in local database
     */
    operator fun invoke(): Flow<Boolean> = flow {
        emit(repository.existDataStored())
    }
}
