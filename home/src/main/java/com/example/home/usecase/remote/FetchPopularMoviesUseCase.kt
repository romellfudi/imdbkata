package com.example.home.usecase.remote

import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to fetch popular movies from remote and store them in database
 */
class FetchPopularMoviesUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Fetch popular movies from remote and store them in database
     * @param query: String? = null
     * @return Flow<Boolean>
     */
    operator fun invoke(query: String? = null): Flow<Boolean> = flow {
        repository.fetchPopular(query).collect {
            when(it){
                is IMDBState.Success -> {
                    val movies = it.data.results
                    repository.storeInDatabase(movies, "popular")
                    movies.forEach { movie ->
                        repository.fetchRecommendationsBy(movie.id).collect {
                            if (it is IMDBState.Success) {
                                val recommendedMovies = it.data.results
                                if (recommendedMovies.isNotEmpty())
                                    repository.updateRecommendedMovies(movie.id,recommendedMovies)
                            }
                        }
                    }
                    emit(true)
                }
                else -> emit(false)
            }
        }
    }
}
