package com.example.home.usecase.local

import com.example.data.models.Genre
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(): Flow<List<Genre>> = flow {
        repository.getGenres().collect {
            emit(it)
        }
    }
}
