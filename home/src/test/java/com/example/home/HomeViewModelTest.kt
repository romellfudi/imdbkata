package com.example.home

import app.cash.turbine.test
import com.example.data.models.Genre
import com.example.data.models.Movie
import com.example.home.ui.viewmodels.HomeViewModel
import com.example.home.usecase.*
import com.example.home.usecase.local.GetGenresUseCase
import com.example.home.usecase.local.GetPopularMoviesUseCase
import com.example.home.usecase.local.GetTopRatedMoviesUseCase
import com.example.home.usecase.remote.FetchGenresUseCase
import com.example.home.usecase.remote.FetchPopularMoviesUseCase
import com.example.home.usecase.remote.FetchTopRatedMoviesUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class HomeViewModelTest {

    private val dispatcherProvider = ViewModelDispatcherProvider()

    @MockK
    lateinit var fetchTopRatedMoviesUseCase: FetchTopRatedMoviesUseCase

    @MockK
    lateinit var fetchGenresUseCase: FetchGenresUseCase

    @MockK
    lateinit var fetchPopularMoviesUseCase: FetchPopularMoviesUseCase

    @MockK
    lateinit var getGenresUseCase: GetGenresUseCase

    @MockK
    lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    @MockK
    lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase

    @MockK
    lateinit var signOutUseCase: SignOutUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test fetch All Data data`() = runBlocking {
        // Create a mock FetchMoviesUseCase object that returns  3 elements list
        val movies = mutableListOf(
            Movie(
                false,
                "/xvYCZ740XvngXK0FNeSNVTJJJ5v.jpg",
                listOf(35),
                729720,
                "en",
                "Finding You",
                "Violinist Finley Sinclair travels to an Irish coastal village to begin her semester studying abroad. At the B&B run by her host family she encounters the gregarious and persistent heartthrob movie star Beckett Rush.",
                299.938,
                "/xvYCZ740XvngXK0FNeSNVTJJJ5v.jpg",
                "2021-05-13",
                "Finding You",
                false,
                7.7,
                54
            )
        )
        val genres = listOf(
            Genre(1, "Action"),
        )
        every { getTopRatedMoviesUseCase.invoke() }.returns(flowOf(movies))
        every { getPopularMoviesUseCase.invoke() }.returns(flowOf(movies))
        every { getGenresUseCase.invoke() }.returns(flowOf(genres))
        every { fetchTopRatedMoviesUseCase.invoke() }.returns(flowOf(true))
        every { fetchPopularMoviesUseCase.invoke() }.returns(flowOf(true))
        every { fetchGenresUseCase.invoke() }.returns(flowOf(true))
        // Create a HomeViewModel object with the mock use case and dispatcher
        val viewModel = HomeViewModel(
            fetchTopRatedMoviesUseCase, fetchPopularMoviesUseCase, fetchGenresUseCase,
            getTopRatedMoviesUseCase, getPopularMoviesUseCase, getGenresUseCase, signOutUseCase,
            dispatcherProvider
        )

        viewModel.loadRemoteData()
        // duplicate the data on the grounds that
        // movieList have the combination of top rated and popular movies
        movies.addAll(movies)
        assertEquals(movies.size, viewModel.movieList.value.size)
        assertEquals(movies, viewModel.movieList.value)

        verify { fetchTopRatedMoviesUseCase.invoke() }
        verify { fetchPopularMoviesUseCase.invoke() }
        verify { fetchGenresUseCase.invoke() }
    }

    @Test
    fun `test fetch Genre data`() = runBlocking {
        // Create a mock FeetGenresUseCase object that returns a list of genres
        val genres = listOf(
            Genre(1, "Action"),
            Genre(2, "Adventure"),
        )
        every { getGenresUseCase.invoke() }.returns(flowOf(genres))

        val genreDict = genres.associate { it.id to it.name }
        // Create a HomeViewModel object with the mock use case and dispatcher
        val viewModel = HomeViewModel(
            fetchTopRatedMoviesUseCase, fetchPopularMoviesUseCase, fetchGenresUseCase,
            getTopRatedMoviesUseCase, getPopularMoviesUseCase, getGenresUseCase, signOutUseCase,
            dispatcherProvider
        )
        viewModel.genresDict.test {
            viewModel.getLocalGenreData()
            assertEquals(genreDict, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the mock use case was invoked
        verify { getGenresUseCase.invoke() }
    }

}
