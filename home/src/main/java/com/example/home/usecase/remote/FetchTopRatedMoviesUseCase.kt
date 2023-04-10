package com.example.home.usecase.remote

import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchTopRatedMoviesUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(query: String? = null): Flow<Boolean> = flow {
        repository.fetchTopRated(query).collect {
            when (it) {
                is IMDBState.Init, is IMDBState.Loading -> emit(false)
                is IMDBState.Success -> {
                    val movies = it.data.results
                    repository.storeInDatabase(movies, "top_rated")
                    movies.forEach { movie ->
                        repository.fetchRecommendationsBy(movie).collect {
                            if (it is IMDBState.Success) {
                                val recommendedMovies = it.data.results
                                if (recommendedMovies.isNotEmpty())
                                    repository.updateRecommendedMovies(movie.id,recommendedMovies)
                            }
                        }
                    }
                    emit(true)
                }
                else ->
                    emit(false)
            }
        }
    }
}
