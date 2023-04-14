package com.example.home.usecase.remote

import com.example.data.models.Movie
import com.example.data.models.MovieDetailResponse
import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMovieDetailUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(id: Int): Flow<MovieDetailResponse> = flow {
        repository.fetchMovieDetail(id).collect {
//            when (it) {
//                is IMDBState.Init, is IMDBState.Loading -> emit(IMDBState.Loading)
            if (it is IMDBState.Success) {
                val movie = it.data
                emit(movie)
            }
//                is IMDBState.Error -> emit(IMDBState.Error(it.error))
        }
    }
}
