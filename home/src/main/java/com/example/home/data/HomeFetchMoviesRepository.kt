package com.example.home.data

import com.example.data.models.Genre
import com.example.data.models.Movie
import com.example.home.data.api.Api
import com.example.home.data.local.GenresDao
import com.example.home.data.local.MoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFetchMoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val genresDao: GenresDao,
    private val api: Api
) {
    suspend fun fetchGenres(query: String?) = withContext(Dispatchers.IO) {
        if (query.isNullOrBlank()) api.getGenres() else api.getGenres(query)
    }

    suspend fun fetchTopRated(query: String?) = withContext(Dispatchers.IO) {
        if (query.isNullOrBlank()) api.getTopRated() else api.getTopRated(query)
    }

    suspend fun fetchPopular(query: String?) = withContext(Dispatchers.IO) {
        if (query.isNullOrBlank()) api.getPopular() else api.getPopular(query)
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

    suspend fun fetchMovieDetail(id: Int) = withContext(Dispatchers.IO) {
        api.getMovieDetailBy(id)
    }

    suspend fun fetchRecommendationsBy(id: Int) = withContext(Dispatchers.IO) {
        api.getRecommendationsBy(id)
    }

}
