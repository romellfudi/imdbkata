package com.example.home.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.data.models.MovieDetailResponse
import com.example.data.models.toCastModel
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieDetailView
import com.example.home.ui.dataview.toMovieView
import com.example.home.usecase.local.InsertFavouriteUseCase
import com.example.home.usecase.local.IsMovieFavouriteUseCase
import com.example.home.usecase.local.RemoveFavouriteUseCase
import com.example.home.usecase.remote.FetchMovieCreditsUseCase
import com.example.home.usecase.remote.FetchMovieDetailUseCase
import com.example.home.usecase.remote.FetchMovieListRecommendationUseCase
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
class HomeMovieViewModel @Inject constructor(
    private val isMovieFavouriteUseCase: IsMovieFavouriteUseCase,
    private val insertFavouriteUseCase: InsertFavouriteUseCase,
    private val removeFavouriteUseCase: RemoveFavouriteUseCase,
    private val fetchMovieDetailUseCase: FetchMovieDetailUseCase,
    private val fetchMovieCreditsUseCase: FetchMovieCreditsUseCase,
    private val fetchMovieRecommendationUseCase: FetchMovieListRecommendationUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading
    private val _movieDetail = mutableNotReplayFlow<MovieDetailView>()
    val movieDetail: SharedFlow<MovieDetailView> = _movieDetail
    private val _isFav = mutableStateOf(false)
    val isFav: State<Boolean> = _isFav

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            _isLoading.value = HomeState.Loading
            combine(
                fetchMovieDetailUseCase(movieId),
                fetchMovieRecommendationUseCase(movieId),
                fetchMovieCreditsUseCase(movieId)
            ) { movieDetail, recommendations, credits ->
                if (movieDetail.id >= 0 && (recommendations.isNotEmpty() || credits.isNotEmpty()))
                    Pair(movieDetail, Pair(credits, recommendations))
                else null
            }.flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message.orEmpty())
                }
                .collect { bundle ->
                    if (bundle != null) {
                        val (movieDetail, pair) = bundle
                        val (credits, recommendations) = pair
                        _movieDetail.tryEmit(
                            MovieDetailView(
                                movieDetail,
                                credits.map { it.toCastModel() },
                                recommendations.map { it.toMovieView() }
                            ))
                        _isLoading.value = HomeState.Ready
                    } else {
                        _isLoading.value = HomeState.Loading
                    }
                }
        }
    }

    fun isUserFav(movieId: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            isMovieFavouriteUseCase(movieId)
                .flowOn(dispatcherProvider.main)
                .collect {
                    _isFav.value = it
                }
        }
    }

    fun toggleFav(favMovie: Boolean, movie: MovieDetailResponse?) {
        if (movie == null) return
        if (favMovie) {
            viewModelScope.launch(dispatcherProvider.main) {
                removeFavouriteUseCase(movie.id)
                    .flowOn(dispatcherProvider.main)
                    .collect {
                        _isFav.value = it
                    }
            }
        } else {
            viewModelScope.launch(dispatcherProvider.main) {
                insertFavouriteUseCase(movie.id,movie.posterUrl)
                    .flowOn(dispatcherProvider.main)
                    .collect {
                        _isFav.value = it
                    }
            }
        }
    }

}