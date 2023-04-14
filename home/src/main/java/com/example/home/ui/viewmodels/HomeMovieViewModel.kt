package com.example.home.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.data.models.MovieDetailResponse
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieDetailView
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.dataview.toMovieView
import com.example.home.usecase.remote.FetchMovieDetailUseCase
import com.example.home.usecase.remote.FetchMovieListRecommendationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val fetchMovieDetailUseCase: FetchMovieDetailUseCase,
    private val fetchMovieRecommendationUseCase: FetchMovieListRecommendationUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading
    private val _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail: LiveData<MovieDetailResponse> = _movieDetail
    private val _recommendations = MutableLiveData<List<MovieView>>()
    val recommendations: LiveData<List<MovieView>> = _recommendations

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            _isLoading.value = HomeState.Loading
            combine(
                fetchMovieDetailUseCase(movieId),
                fetchMovieRecommendationUseCase(movieId)
            ) { movieDetail, recommendations ->
                if (movieDetail.id >= 0 && recommendations.isNotEmpty())
                    Pair(movieDetail, recommendations)
                else null
            }.flowOn(dispatcherProvider.main)
                .catch {
                    _isLoading.value = HomeState.Error(it.message.orEmpty())
                }
                .collect { pair ->
                    if (pair != null) {
                        val (movieDetail, recommendations) = pair
                        _movieDetail.value = movieDetail
                        _recommendations.value = recommendations.map { it.toMovieView() }
                        _isLoading.value = HomeState.Ready
                    } else {
                        _isLoading.value = HomeState.Loading
                    }
                }
        }
    }

}