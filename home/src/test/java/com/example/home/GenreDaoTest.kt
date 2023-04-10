package com.example.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.models.Genre
import com.example.home.data.local.GenresDao
import com.example.home.data.local.MovieDataBase
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
class GenreDaoTest {

    private lateinit var genresDao: GenresDao
    private lateinit var db: MovieDataBase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDataBase::class.java
        ).build()
        genresDao = db.genreDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `test replaceTable and deleteAll functions`() = runBlocking {
        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Adventure"),
            Genre(3, "Animation"),
            Genre(4, "Comedy"),
            Genre(5, "Crime"),
            Genre(6, "Documentary"),
            Genre(7, "Drama"),
            Genre(8, "Family"),
            Genre(9, "Fantasy"),
            Genre(10, "History"),
            Genre(11, "Horror"),
            Genre(12, "Music"),
            Genre(13, "Mystery"),
            Genre(14, "Romance"),
            Genre(15, "Science Fiction"),
            Genre(16, "TV Movie"),
            Genre(17, "Thriller"),
            Genre(18, "War"),
            Genre(19, "Western")
        )
        withContext(Dispatchers.IO) {
            genresDao.replaceTable(genres)
            val result = genresDao.getAllGenres().first()
            assertEquals(genres.size, result.size)
            assertEquals(genres, result)
            genresDao.deleteAll()
            val result2 = genresDao.getAllGenres().first()
            assertEquals(emptyList<Genre>(), result2)
        }
    }

}
