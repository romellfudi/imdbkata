package com.example.home.usecase.local

import com.example.data.models.UserFav
import com.example.home.data.FirebaseUserRepository
import com.example.home.data.HomeFetchMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case to get all favourite movies from local database
 */
class GetAllFavouriteUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository,
    private val repository: HomeFetchMoviesRepository
) {
    /**
     * Invoke the use case to get all favourite movies from local database
     * @return [Flow] of [List] of [UserFav]
     */
    operator fun invoke(): Flow<List<UserFav>> = flow {
        firebaseUserRepository.getUserLogged()?.let { user ->
            repository.getAllFavBy(user.email).collect {
                emit(it)
            }
        } ?: emit(emptyList())
    }
}
