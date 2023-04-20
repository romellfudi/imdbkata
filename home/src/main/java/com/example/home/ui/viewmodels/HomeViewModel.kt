package com.example.home.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.data.models.Movie
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.dataview.toMovieView
import com.example.home.usecase.firebase.SignOutUseCase
import com.example.home.usecase.local.*
import com.example.home.usecase.remote.FetchGenresUseCase
import com.example.home.usecase.remote.FetchPopularMoviesUseCase
import com.example.home.usecase.remote.FetchTopRatedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchTopRatedMoviesUseCase: FetchTopRatedMoviesUseCase,
    private val fetchPopularMoviesUseCase: FetchPopularMoviesUseCase,
    private val fetchGenresUseCase: FetchGenresUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val cleanGuestFavUseCase: CleanGuestFavUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _movieList = mutableStateOf<List<Movie>>(emptyList())
    val movieList: State<List<Movie>> = _movieList
    private val _genresDict = mutableNotReplayFlow<Map<Int, String>>()
    val genresDict: SharedFlow<Map<Int, String>> = _genresDict
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading
    private val _shouldGoToLogin = mutableNotReplayFlow<Boolean>()
    val shouldGoToLogin: SharedFlow<Boolean> = _shouldGoToLogin

    fun cleanGuestData() {
        viewModelScope.launch(dispatcherProvider.main) {
            cleanGuestFavUseCase()
        }
    }

    fun loadRemoteData() {
        viewModelScope.launch(dispatcherProvider.main) {
            combine(
                fetchTopRatedMoviesUseCase(),
                fetchPopularMoviesUseCase(),
                fetchGenresUseCase()
            ) { t, p, g -> (t || p) && g }
                .flowOn(dispatcherProvider.main)
                .onStart { _isLoading.value = HomeState.Loading }
                .catch { _isLoading.value = HomeState.Error(it.message ?: "Error") }
                .collect {
                    if (it) {
                        viewModelScope.launch(dispatcherProvider.io) {
                            getLocalGenreData()
                            getLocalMovieData()
                        }
                    }
                }
        }
    }

    private fun getLocalMovieData() {
        viewModelScope.launch(dispatcherProvider.main) {
            combine(
                getTopRatedMoviesUseCase(),
                getPopularMoviesUseCase()
            ) { t, p -> t.plus(p) }
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect {
                    _movieList.value = it
                    _isLoading.value = HomeState.Ready
                }
        }
    }

    fun getLocalGenreData() {
        viewModelScope.launch(dispatcherProvider.main) {
            getGenresUseCase()
                .flowOn(dispatcherProvider.main)
                .collect {
                    if (it.isNotEmpty()) {
                        // mapper elements from genre list to map
                        _genresDict.tryEmit(it.associate { genre -> genre.id to genre.name })
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatcherProvider.main) {
            signOutUseCase()
                .flowOn(dispatcherProvider.main)
                .catch { _shouldGoToLogin.tryEmit(false) }
                .collect { _shouldGoToLogin.tryEmit(it) }
        }
    }

}