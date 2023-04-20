package com.example.home.usecase.local

import com.example.data.models.Movie
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to get top rated movies from local database
 */
class GetTopRatedMoviesUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke use case to get top rated movies from local database
     * @return [Flow] of [List] of [Movie]
     */
    operator fun invoke(): Flow<List<Movie>> = flow {
        repository.getTopRatedMovies("top_rated").collect {
            emit(it)
        }
    }
}
