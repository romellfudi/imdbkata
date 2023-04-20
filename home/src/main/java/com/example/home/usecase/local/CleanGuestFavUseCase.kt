package com.example.home.usecase.local

import com.example.home.data.FirebaseUserRepository
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to clean guest favorite movies
 */
class CleanGuestFavUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke the use case to clean guest favorite movies
     * @return [Flow] with [Boolean] as result
     */
    operator fun invoke(): Flow<Boolean> = flow {
        firebaseUserRepository.getUserLogged()?.let {
            val result = repository.deleteGuestFav()
            emit(result > 0)
        }?: emit(false)
    }
}
