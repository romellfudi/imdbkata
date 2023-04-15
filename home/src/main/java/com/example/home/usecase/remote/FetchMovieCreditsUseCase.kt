package com.example.home.usecase.remote

import com.example.data.models.CastResponse
import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMovieCreditsUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(id: Int): Flow<List<CastResponse>> = flow {
        repository.fetchCreditsBy(id).collect {
            if (it is IMDBState.Success) {
                val movieCast = it.data.cast
                emit(movieCast)
            }
        }
    }
}
