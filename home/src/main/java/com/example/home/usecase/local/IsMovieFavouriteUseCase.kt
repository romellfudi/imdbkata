package com.example.home.usecase.local

import com.example.home.data.FirebaseUserRepository
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to remove a movie from the favourite list
 */
class IsMovieFavouriteUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke the use case to remove a movie from the favourite list
     * @param movieId the movie id
     * @return a [Flow] of [Boolean] that indicates if the movie is favourite or not
     */
    operator fun invoke(movieId: Int): Flow<Boolean> = flow {
        firebaseUserRepository.getUserLogged()?.let { user ->
            repository.getAllFavBy(user.email).collect { list ->
                emit(list.map { it.movieId }.contains(movieId))
            }
        } ?: emit(false)
    }
}
