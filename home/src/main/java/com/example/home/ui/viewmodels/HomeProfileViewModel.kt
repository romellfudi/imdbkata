package com.example.home.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.dataview.UserView
import com.example.home.ui.dataview.toMovieView
import com.example.home.ui.dataview.toUserView
import com.example.home.usecase.firebase.GetFirebaseUserUseCase
import com.example.home.usecase.local.GetAllFavouriteUseCase
import com.example.home.usecase.local.GetPopularMoviesUseCase
import com.example.home.usecase.local.GetTopRatedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltViewModel
class HomeProfileViewModel @Inject constructor(
    private val getFirebaseUserUseCase: GetFirebaseUserUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getAllFavouriteUseCase: GetAllFavouriteUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _user = mutableNotReplayFlow<UserView?>()
    val user: SharedFlow<UserView?> = _user
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading
    private val _movieList = mutableNotReplayFlow<List<MovieView>>()
    val movieList: SharedFlow<List<MovieView>> = _movieList
    private val _userFavList = mutableNotReplayFlow<List<Int>>()
    val userFavList: SharedFlow<List<Int>> = _userFavList

    fun loadFav() {
        viewModelScope.launch(dispatcherProvider.main) {
            getAllFavouriteUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect {
                    _userFavList.emit(it.map { fav -> fav.movieId })
                }
        }
    }

    fun getLocalMovieData() {
        viewModelScope.launch(dispatcherProvider.main) {
            combine(
                getPopularMoviesUseCase(),
                getTopRatedMoviesUseCase(),
            ) { t, p -> t.plus(p) }
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect {
                    _movieList.tryEmit(it.map { it.toMovieView() })
                }
        }
    }


    fun fetchUser() {
        viewModelScope.launch(dispatcherProvider.main) {
            getFirebaseUserUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _user.emit(null)
                }
                .collect { state ->
                    _user.emit(state?.toUserView())
                }
        }
    }

}