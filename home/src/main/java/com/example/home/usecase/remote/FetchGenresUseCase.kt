package com.example.home.usecase.remote

import com.example.home.data.HomeFetchMoviesRepository
import com.example.home.data.api.IMDBState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchGenresUseCase @Inject constructor(
    private val homeFetchMoviesRepository: HomeFetchMoviesRepository
) {
    operator fun invoke(): Flow<Boolean> = flow {
        homeFetchMoviesRepository.fetchGenres().collect {
            if (it is IMDBState.Success) {
                homeFetchMoviesRepository.storeGenresInDatabase(it.data.genres)
                emit(true)
            } else emit(false)
        }
    }
}
