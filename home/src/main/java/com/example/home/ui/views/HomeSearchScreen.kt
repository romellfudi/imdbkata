package com.example.home.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.example.core.view.*
import com.example.core.view.compose.FailedAnimation
import com.example.core.view.compose.LoadingAnimation
import com.example.home.R
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.viewmodels.HomeSearchViewModel

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeSearchScreen(
    viewModel: HomeSearchViewModel,
    goToDetail: (Int) -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {

    val genresDict = viewModel.genresDict.collectAsState(emptyMap())
    val filteredMovies by viewModel.filteredMovieList.observeAsState(emptyList())
    val isLoading = remember { viewModel.isLoading }
    val queryValue: String by viewModel.query.observeAsState(initial = "")

    LaunchedEffect("Load Movies") {
        viewModel.loadLocalDataOrFetch()
    }

    when (isLoading.value) {
        is HomeState.Loading -> {
            LoadingAnimation(modifier = Modifier.fillMaxSize())
        }
        is HomeState.Error -> {
            FailedAnimation(modifier = Modifier.fillMaxSize())
        }
        is HomeState.Ready -> {
            MoviesList(
                queryValue = queryValue,
                viewModel = viewModel,
                movies = filteredMovies,
                genres = genresDict.value,
                goToDetail = goToDetail,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun MoviesList(
    queryValue: String,
    viewModel: HomeSearchViewModel,
    movies: List<MovieView>,
    genres: Map<Int, String>,
    goToDetail: (Int) -> Unit,
    isDark: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val state = rememberLazyListState()
        val (search, listView) = createRefs()

        Row(modifier = Modifier
            .constrainAs(search) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
            .padding(padding_16)) {
            TextField(
                colors = getTextFieldColors(isDark),
                singleLine = true,
                modifier = Modifier
                    .semantics { testTag = "home_screen_search_field" }
                    .fillMaxWidth(),
                value = queryValue,
                onValueChange = { viewModel.filterMovies(it) },
                label = { Text(stringResource(R.string.search_movies)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") }
            )
        }
        LazyColumn(
            state = state,
            modifier = modifier
                .constrainAs(listView) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(search.bottom, padding_32)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(5.dp)
                .semantics { testTag = "home_screen_list" },
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(movies) {
                MovieCard(movie = it, genres = genres, goToDetail = goToDetail)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieCard(
    movie: MovieView,
    genres: Map<Int, String>,
    goToDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { goToDetail(movie.id) },
        elevation = 3.dp,
        backgroundColor = Color3,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {

        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter =
                rememberAsyncImagePainter(movie.posterUrl),
                contentDescription = "Movie poster",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(1.dp)
                    .size(130.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    style = TextStyle(
                        color = Color2,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    text = movie.releaseDate,
                    style = TextStyle(
                        color = Color4,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Spacer(modifier = Modifier.height(padding_6))
                Text(
                    text = movie.genreIds.map { genres[it] }.joinToString(", "),
                    style = TextStyle(
                        color = Color4,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}