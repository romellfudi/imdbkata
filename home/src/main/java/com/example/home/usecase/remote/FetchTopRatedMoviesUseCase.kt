package com.example.home.usecase.remote

import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to fetch top rated movies from remote and store them in database
 */
class FetchTopRatedMoviesUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Fetch top rated movies from remote and store them in database
     * @param query: String? = null
     * @return Flow<Boolean>
     */
    operator fun invoke(query: String? = null): Flow<Boolean> = flow {
        repository.fetchTopRated(query).collect {
            when (it) {
                is IMDBState.Success -> {
                    val movies = it.data.results
                    repository.storeInDatabase(movies, "top_rated")
                    movies.forEach { movie ->
                        repository.fetchRecommendationsBy(movie.id).collect { state ->
                            if (state is IMDBState.Success) {
                                val recommendedMovies = state.data.results
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
