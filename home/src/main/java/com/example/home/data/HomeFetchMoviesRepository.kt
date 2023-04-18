package com.example.home.data

import com.example.data.models.Genre
import com.example.data.models.Movie
import com.example.home.data.api.GenreApi
import com.example.home.data.api.MovieApi
import com.example.home.data.api.MovieListApi
import com.example.home.data.local.GenresDao
import com.example.home.data.local.MoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFetchMoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val genresDao: GenresDao,
    private val movieListApi: MovieListApi,
    private val movieApi: MovieApi,
    private val genreApi: GenreApi
) {
    suspend fun fetchGenres() = withContext(Dispatchers.IO) {
        genreApi.getGenres()
    }

    suspend fun fetchTopRated(query: String?) = withContext(Dispatchers.IO) {
        if (query.isNullOrBlank()) movieListApi.getTopRated() else movieListApi.getTopRated(query)
    }

    suspend fun fetchPopular(query: String?) = withContext(Dispatchers.IO) {
        if (query.isNullOrBlank()) movieListApi.getPopular() else movieListApi.getPopular(query)
    }

    suspend fun fetchMovieDetail(id: Int) = withContext(Dispatchers.IO) {
        movieApi.getMovieDetailBy(id)
    }

    suspend fun fetchRecommendationsBy(id: Int) = withContext(Dispatchers.IO) {
        movieApi.getRecommendationsBy(id)
    }

    suspend fun fetchCreditsBy(id: Int) = withContext(Dispatchers.IO) {
        movieApi.getCreditsBy(id)
    }

    suspend fun storeInDatabase(movies: List<Movie>, type: String) = withContext(Dispatchers.IO) {
        moviesDao.insertAllMovies(movies.map { it.copy(typeRank = type) })
    }

    suspend fun storeGenresInDatabase(genres: List<Genre>) = withContext(Dispatchers.IO) {
        genresDao.replaceTable(genres)
    }

    suspend fun getGenres() = withContext(Dispatchers.IO) {
        genresDao.getAllGenres()
    }

    suspend fun getTopRatedMovies(typeRank: String) = withContext(Dispatchers.IO) {
        moviesDao.getAllMoviesByType(typeRank)
    }

    suspend fun updateRecommendedMovies(id: Int, recommendedMovies: List<Movie>) = withContext(Dispatchers.IO)  {
        moviesDao.updateRecommendedMovies(id,recommendedMovies)
    }

    suspend fun existDataStored() = withContext(Dispatchers.IO) {
        moviesDao.existDataStored()
    }

}
