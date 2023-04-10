package com.example.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.models.Movie
import com.example.home.data.local.MovieDataBase
import com.example.home.data.local.MoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class MovieDaoTest {

    private lateinit var moviesDao: MoviesDao
    private lateinit var db: MovieDataBase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDataBase::class.java
        ).build()
        moviesDao = db.movieDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `test insert and get movie`() = runBlocking {
        val movie  = Movie(
        false,
        "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
        listOf(18, 80),
        238,
        "en",
        "The Godfather",
        "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.",
        165.077,
        "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
        "1972-03-14",
        "The Godfather",
        false,
        8.7,
        17597)
        withContext(Dispatchers.IO) {
            moviesDao.replaceTable(listOf(movie))
            val result = moviesDao.getMovie(238).first()
            assertEquals(movie, result)
        }
    }

    @Test
    fun `test replaceTable and deleteAll functions`() = runBlocking {
        val movies = listOf(
            Movie(false, "/xvYCZ740XvngXK0FNeSNVTJJJ5v.jpg", listOf(35), 729720, "en", "Finding You", "Violinist Finley Sinclair travels to an Irish coastal village to begin her semester studying abroad. At the B&B run by her host family she encounters the gregarious and persistent heartthrob movie star Beckett Rush.", 299.938, "/xvYCZ740XvngXK0FNeSNVTJJJ5v.jpg", "2021-05-13", "Finding You", false, 7.7, 54),
            Movie(true, "/7PrzMEYjFyTuYeMXPwQ217oE1cK.jpg", listOf(35), 623452, "en", "Twist", "A Dickens classic brought thrillingly up to date in the teeming heartland of modern London, where a group of street smart young hustlers plan the heist of the century for the ultimate payday.", 158.676, "/cKKf5ARFVur6EudbBiUILaWxR8E.jpg", "2021-01-22", "Twist", false, 6.7, 221),
            Movie(false, "/7W0G3YECgDAfnuiHG91r8WqgIOe.jpg", listOf(35, 10749), 761053, "en", "Gabby Duran and the Unsittables", "Gabby Duran, who constantly feels like she’s living in the shadows of her mother and younger sister, finally finds her moment to shine when she inadvertently lands an out-of-this-world job to babysit an unruly group of very important extraterrestrial children who are hiding out on Earth with their families, disguised as everyday kids.", 161.666, "/6Uqp1LZJSFP6UwG6uX2CkA9Cdz6.jpg", "2019-10-11", "Gabby Duran and the Unsittables", false, 6.9, 11)
        )
        withContext(Dispatchers.IO) {
            moviesDao.replaceTable(movies)
            val allMoviesBeforeDelete = moviesDao.getAllMovies().first()
            assertEquals(movies.size, allMoviesBeforeDelete.size)
            moviesDao.deleteAll()
            val allMoviesAfterDelete = moviesDao.getAllMovies().first()
            assertEquals(emptyList<Movie>(), allMoviesAfterDelete)
        }
    }

}
