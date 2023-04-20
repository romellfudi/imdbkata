package com.example.home.usecase.local

import com.example.home.data.FirebaseUserRepository
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveFavouriteUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val repository: HomeFetchMoviesRepository
) {
    operator fun invoke(movieId: Int): Flow<Boolean> = flow {
        firebaseUserRepository.getUserLogged()?.let {
            val result = repository.deleteFav(it.email, movieId)
            emit(false)
        } ?: emit(true)
    }
}
