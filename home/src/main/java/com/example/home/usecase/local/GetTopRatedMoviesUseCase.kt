package com.example.home.usecase.local

import com.example.data.models.Movie
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(): Flow<List<Movie>> = flow {
        repository.getTopRatedMovies("top_rated").collect {
            emit(it)
        }
    }
}
