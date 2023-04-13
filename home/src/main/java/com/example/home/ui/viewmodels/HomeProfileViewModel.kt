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
import com.example.home.usecase.*
import com.example.home.usecase.local.ExistLocalDataUseCase
import com.example.home.usecase.local.GetGenresUseCase
import com.example.home.usecase.local.GetPopularMoviesUseCase
import com.example.home.usecase.local.GetTopRatedMoviesUseCase
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
class HomeProfileViewModel @Inject constructor(
    private val existLocalDataUseCase: ExistLocalDataUseCase,
    private val fetchTopRatedMoviesUseCase: FetchTopRatedMoviesUseCase,
    private val fetchPopularMoviesUseCase: FetchPopularMoviesUseCase,
    private val fetchGenresUseCase: FetchGenresUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _movieList = mutableStateOf<List<Movie>>(emptyList())
    val movieList: State<List<Movie>> = _movieList
    private val _filteredMovieList = mutableNotReplayFlow<List<MovieView>>()
    val filteredMovieList: SharedFlow<List<MovieView>> = _filteredMovieList
    private val _genresDict = mutableNotReplayFlow<Map<Int, String>>()
    val genresDict: SharedFlow<Map<Int, String>> = _genresDict
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading
    private val _shouldGoToLogin = mutableNotReplayFlow<Boolean>()
    val shouldGoToLogin: SharedFlow<Boolean> = _shouldGoToLogin
    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

    companion object {
        const val DEBOUNCE_PERIOD = 500L
    }

    init {
        search()
    }

    fun filterMovies(query: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            search(query)
        }
    }

    private fun search(query: String = "") {
        _query.value = query
        viewModelScope.launch(dispatcherProvider.main) {
            _query
                .asFlow()
                .debounce(DEBOUNCE_PERIOD)
                .flowOn(dispatcherProvider.main)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    val filtered = if (query.isNotEmpty()) {
                        movieList.value.filter { it.title.contains(query, ignoreCase = true) }
                    } else {
                        movieList.value
                    }
                    flowOf(filtered.map { it.toMovieView() })
                }
                .catch {
                    _filteredMovieList.tryEmit(emptyList())
                }
                .collect {
                    _filteredMovieList.tryEmit(it)
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
                        getLocalGenreData()
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
                        getLocalGenreData()
                        getLocalMovieData()
                    } else {
                        _isLoading.value = HomeState.Loading
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
                    filterMovies(query = "")
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
                        _genresDict.tryEmit(it.associate { it.id to it.name })
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatcherProvider.main) {
            signOutUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _shouldGoToLogin.tryEmit(false)
                }
                .collect {
                    _shouldGoToLogin.tryEmit(it)
                }
        }
    }

    fun onMovieClicked(movie: Movie) {
//        repository.insertMovie(movie)
    }

}