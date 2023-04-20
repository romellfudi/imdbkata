package com.example.home.usecase.remote

import com.example.data.models.Movie
import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to fetch movie recommendations
 */
class FetchMovieListRecommendationUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke the use case to fetch movie recommendations
     * @param id movie id
     * @return [Flow] of [List] of [Movie]
     */
    operator fun invoke(id: Int): Flow<List<Movie>> = flow {
        repository.fetchRecommendationsBy(id).collect {
            if (it is IMDBState.Success) {
                val recommendations = it.data.results
                emit(recommendations)
            }
        }
    }
}
