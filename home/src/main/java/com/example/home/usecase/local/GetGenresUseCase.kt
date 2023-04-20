package com.example.home.usecase.local

import com.example.data.models.Genre
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to get genres from local database
 */
class GetGenresUseCase @Inject constructor(
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Get genres from local database
     * @return [Flow] of [List] of [Genre]
     */
    operator fun invoke(): Flow<List<Genre>> = flow {
        repository.getGenres().collect {
            emit(it)
        }
    }
}
