package com.example.home

import com.example.data.models.Movie
import com.example.data.models.MovieResponse
import com.example.home.data.api.MovieListApi
import com.example.home.data.api.IMDBState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class MovieListApiRestTest {

    @Mock
    internal val api: MovieListApi = mock()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test getting movies from api`() = runBlocking {
        // create mock response
        val mockResponse = MovieResponse(
            page = 1,
            results = listOf(
                Movie(
                    adult = false,
                    backdropPath = "",
                    genreIds = listOf(),
                    id = 1,
                    originalLanguage = "",
                    originalTitle = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    releaseDate = "",
                    title = "",
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        // Given
        val key = "key"
        whenever(api.getTopRated(key)).thenReturn(flowOf(IMDBState.Success(null, mockResponse)))

        // Then
        val response = api.getTopRated(key).first()

        assertTrue(response is IMDBState.Success)

        val data = (response as IMDBState.Success).data
        assertEquals(mockResponse, data)
    }

    @Test
    fun `test getting error from the api`() = runBlocking {
        // Given
        val key = "key"
        val errorMessage = "Error getting movies"
        whenever(api.getTopRated(key)).thenReturn(flowOf(IMDBState.Error(errorMessage)))

        // When
        val response = api.getTopRated(key).first()

        // Then
        assertTrue(response is IMDBState.Error)
        assertEquals(errorMessage, (response as IMDBState.Error).errorMessage)
    }

    @Test
    fun `test get loading state from the api`() = runBlocking {
        // Given
        val key = "key"
        whenever(api.getTopRated(key)).thenReturn(flowOf(IMDBState.Loading()))

        // When
        val response = api.getTopRated(key).first()

        // Then
        assertTrue(response is IMDBState.Loading)
    }

    @Test
    fun `test the a list of movies from the api`() = runBlocking {
        // create mock response
        val mockResponse = MovieResponse(
            page = 1,
            results = listOf(
                Movie(
                    adult = false,
                    backdropPath = "",
                    genreIds = listOf(),
                    id = 1,
                    originalLanguage = "",
                    originalTitle = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    releaseDate = "",
                    title = "",
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                ),
                Movie(
                    adult = false,
                    backdropPath = "",
                    genreIds = listOf(),
                    id = 2,
                    originalLanguage = "",
                    originalTitle = "",
                    overview = "",
                    popularity = 0.0,
                    posterPath = "",
                    releaseDate = "",
                    title = "",
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            ),
            totalPages = 1,
            totalResults = 2
        )

        // Given
        val key = "key"
        whenever(api.getTopRated(key)).thenReturn(flowOf(IMDBState.Success(null, mockResponse)))

        // When
        val response = api.getTopRated(key).first()

        // Then
        assertTrue(response is IMDBState.Success)

        val data = (response as IMDBState.Success).data
        assertEquals(mockResponse.page, data.page)
        assertEquals(mockResponse.results.size, data.results.size)
        assertEquals(mockResponse.results[0], data.results[0])
        assertEquals(mockResponse.results[1], data.results[1])
    }

}
