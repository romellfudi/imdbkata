package com.example.home.usecase.local

import com.example.home.data.FirebaseUserRepository
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to remove a movie from the favourite list
 */
class RemoveFavouriteUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke the use case to remove a movie from the favourite list
     * @param movieId The movie id to remove
     * @return A [Flow] with a [Boolean] value. If the value is true, the user is not logged in.
     */
    operator fun invoke(movieId: Int): Flow<Boolean> = flow {
        firebaseUserRepository.getUserLogged()?.let {
            val result = repository.deleteFav(it.email, movieId)
            emit(false)
        } ?: emit(true)
    }
}
