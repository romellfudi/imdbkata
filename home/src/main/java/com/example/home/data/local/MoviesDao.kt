package com.example.home.data.local

import androidx.room.*
import com.example.data.models.Movie
import kotlinx.coroutines.flow.Flow

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies where typeRank = :typeRank")
    fun getAllMoviesByType(typeRank: String): Flow<List<Movie>>

    @Query("DELETE FROM movies")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(data: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllMoviesRecommended(data: List<Movie>)

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovie(id: Int): Flow<Movie>

    @Query("UPDATE movies SET recommended_movies = :recommendedMovies WHERE id = :id")
    fun updateMovieRecommendedMovies(id:Int, recommendedMovies: List<Int>)

    @Transaction
    fun updateRecommendedMovies(id:Int, recommendedMovies: List<Movie>) {
        val listRecommendIds = recommendedMovies.map { it.id }
        updateMovieRecommendedMovies(id, listRecommendIds)
        insertAllMoviesRecommended(recommendedMovies)
    }

    @Transaction
    fun replaceTable(data: List<Movie>) {
        deleteAll()
        insertAllMovies(data)
    }

    // fun to check if exist at least one row in the table
    @Query("SELECT EXISTS(SELECT 1 FROM movies LIMIT 1)")
    fun existDataStored(): Boolean
}
