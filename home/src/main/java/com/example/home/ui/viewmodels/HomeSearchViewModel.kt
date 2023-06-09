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
class HomeSearchViewModel @Inject constructor(
    private val existLocalDataUseCase: ExistLocalDataUseCase,
    private val fetchTopRatedMoviesUseCase: FetchTopRatedMoviesUseCase,
    private val fetchPopularMoviesUseCase: FetchPopularMoviesUseCase,
    private val fetchGenresUseCase: FetchGenresUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val getAllFavouriteUseCase: GetAllFavouriteUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _movieList = mutableStateOf<List<Movie>>(emptyList())
    private val movieList: State<List<Movie>> = _movieList
    private val _filteredMovieList = MutableLiveData<List<MovieView>>()
    val filteredMovieList: LiveData<List<MovieView>> = _filteredMovieList
    private val _genresDict = mutableNotReplayFlow<Map<Int, String>>()
    val genresDict: SharedFlow<Map<Int, String>> = _genresDict
    private val _userFavList = mutableNotReplayFlow<List<Int>>()
    val userFavList: SharedFlow<List<Int>> = _userFavList
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading
    private val _query = MutableLiveData<String>()
    val query: LiveData<String> = _query

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

    fun filterMovies(query: String) {
        viewModelScope.launch(dispatcherProvider.main) {
            search(query)
        }
    }

    fun search(query: String = "") {
        _query.value = query
        viewModelScope.launch(dispatcherProvider.main) {
            _query.asFlow()
                .debounce(DEBOUNCE_PERIOD)
                .flowOn(dispatcherProvider.main)
                .map { query ->
                    if (query.isEmpty()) movieList.value
                    else movieList.value.filter { it.title.contains(query.trim(), ignoreCase = true) }
                }
                .map { it.map { movie -> movie.toMovieView() } }
                .onEach {
                    _filteredMovieList.value = it
                    _isLoading.value = HomeState.Ready
                }
                .catch {
                    _filteredMovieList.value = emptyList()
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }
                .launchIn(viewModelScope)

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

    private fun loadRemoteData() {
        viewModelScope.launch(dispatcherProvider.io) {
            combine(
                fetchPopularMoviesUseCase(),
                fetchTopRatedMoviesUseCase(),
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
                getPopularMoviesUseCase(),
            getTopRatedMoviesUseCase(),
            ) { p, t -> p.plus(t) }
                .flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message ?: "Error")
                }.collect {
                    _movieList.value = it
                    filterMovies(query = _query.value.orEmpty())
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

}