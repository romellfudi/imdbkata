package com.example.home.data.api

import com.example.data.models.GenresResponse
import com.example.home.BuildConfig
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
interface GenreApi {

    @GET("genre/movie/list")
    fun getGenres(@Query("api_key") key: String = BuildConfig.IMDB_API_KEY)
            : Flow<IMDBState<GenresResponse>>

}