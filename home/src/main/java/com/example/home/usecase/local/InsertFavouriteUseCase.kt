package com.example.home.usecase.local

import com.example.home.data.FirebaseUserRepository
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to insert a movie as favourite
 */
class InsertFavouriteUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Insert a movie as favourite
     * @param movieId Id of the movie
     * @param posterPath Poster path of the movie
     * @return True if the movie was inserted as favourite, false otherwise
     */
    operator fun invoke(movieId: Int,posterPath:String): Flow<Boolean> = flow {
        firebaseUserRepository.getUserLogged()?.let {
            val result = repository.insertFav(it.email, movieId, posterPath)
            emit(result > 0 )
        } ?: emit(false)
    }
}
