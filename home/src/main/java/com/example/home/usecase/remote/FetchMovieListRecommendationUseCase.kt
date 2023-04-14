package com.example.home.usecase.remote

import com.example.data.models.Movie
import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMovieListRecommendationUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(id: Int): Flow<List<Movie>> = flow {
        repository.fetchRecommendationsBy(id).collect {
            if (it is IMDBState.Success) {
                val recommendations = it.data.results
                emit(recommendations)
            }
        }
    }
}
