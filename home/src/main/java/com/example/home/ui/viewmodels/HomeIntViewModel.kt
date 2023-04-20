package com.example.home.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.dataview.toMovieView
import com.example.home.usecase.local.ExistLocalDataUseCase
import com.example.home.usecase.local.GetAllFavouriteUseCase
import com.example.home.usecase.local.GetPopularMoviesUseCase
import com.example.home.usecase.local.GetTopRatedMoviesUseCase
import com.example.home.usecase.remote.FetchGenresUseCase
import com.example.home.usecase.remote.FetchPopularMoviesUseCase
import com.example.home.usecase.remote.FetchTopRatedMoviesUseCase
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
class HomeIntViewModel @Inject constructor(
    private val existLocalDataUseCase: ExistLocalDataUseCase,
    private val fetchTopRatedMoviesUseCase: FetchTopRatedMoviesUseCase,
    private val fetchPopularMoviesUseCase: FetchPopularMoviesUseCase,
    private val fetchGenresUseCase: FetchGenresUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getAllFavouriteUseCase: GetAllFavouriteUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _movieTopRatedList = mutableNotReplayFlow<List<MovieView>>()
    val movieTopRatedList: SharedFlow<List<MovieView>> = _movieTopRatedList
    private val _moviePopularList = mutableNotReplayFlow<List<MovieView>>()
    val moviePopularList: SharedFlow<List<MovieView>> = _moviePopularList
    private val _userFavList = mutableNotReplayFlow<List<Int>>()
    val userFavList: SharedFlow<List<Int>> = _userFavList
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading

    companion object {
        const val DEBOUNCE_PERIOD = 500L
    }

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

    fun loadLocalDataOrFetch() {
        viewModelScope.launch(dispatcherProvider.main) {
            existLocalDataUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect { existLocalData ->
                    if (existLocalData) {
                        getLocalMovieData()
                    } else {
                        loadRemoteData()
                    }

                }
        }
    }

    fun loadRemoteData() {
        viewModelScope.launch(dispatcherProvider.io) {
            combine(
                fetchTopRatedMoviesUseCase(),
                fetchPopularMoviesUseCase(),
                fetchGenresUseCase()
            ) { t, p, g -> (t || p) && g }
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect {
                    if (it) {
                        getLocalMovieData()
                    } else {
                        _isLoading.value = HomeState.Loading
                    }
                }
        }
    }

    private fun getLocalMovieData() {
        viewModelScope.launch(dispatcherProvider.main) {
            getTopRatedMoviesUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect { movies ->
                    _movieTopRatedList.tryEmit(movies.map { it.toMovieView() })
                    _isLoading.value = HomeState.Ready
                }
        }
        viewModelScope.launch(dispatcherProvider.main) {
            getPopularMoviesUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect { movies ->
                    _moviePopularList.tryEmit(movies.map { it.toMovieView() })
                    _isLoading.value = HomeState.Ready
                }
        }
    }

}