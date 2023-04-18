package com.example.home.data.api

import com.example.data.models.CreditsResponse
import com.example.data.models.MovieDetailResponse
import com.example.data.models.MovieResponse
import com.example.home.BuildConfig
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
interface MovieApi {

    @GET("movie/{movie_id}")
    fun getMovieDetailBy(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.IMDB_API_KEY
    ): Flow<IMDBState<MovieDetailResponse>>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendationsBy(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.IMDB_API_KEY
    ): Flow<IMDBState<MovieResponse>>

    @GET("movie/{movie_id}/credits")
    fun getCreditsBy(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.IMDB_API_KEY
    ): Flow<IMDBState<CreditsResponse>>
}