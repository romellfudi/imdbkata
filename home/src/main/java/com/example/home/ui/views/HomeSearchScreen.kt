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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    toMovieDetail: (Int) -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {

    val genresDict = viewModel.genresDict.collectAsState(emptyMap())
    val filteredMovies by viewModel.filteredMovieList.observeAsState(emptyList())
    val userFavList by viewModel.userFavList.collectAsState(emptyList())
    val isLoading = remember { viewModel.isLoading }
    val queryValue: String by viewModel.query.observeAsState(initial = "")

    LaunchedEffect("Load Movies") {
        viewModel.loadLocalDataOrFetch()
        viewModel.loadFav()
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
                movies = filteredMovies.map { movie ->
                    movie.copy(isFav = userFavList.any { it == movie.id })
                },
                genres = genresDict.value,
                toMovieDetail = toMovieDetail,
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
    toMovieDetail: (Int) -> Unit,
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
                MovieCard(movieView = it, genres = genres, toMovieDetail = toMovieDetail)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieCard(
    movieView: MovieView,
    genres: Map<Int, String>,
    toMovieDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth(),
        onClick = { toMovieDetail(movieView.id) },
        elevation = 3.dp,
        backgroundColor = ColorDarkSecondary,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(padding_6)
                .fillMaxWidth()
        ) {
            val (movieCard, plus, title, date, genresView) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movieView.posterUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = "movie poster",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(160.dp)
                    .width(110.dp)
                    .constrainAs(movieCard) {
                        start.linkTo(parent.start, no_padding)
                        top.linkTo(parent.top)
                    }
            )
            Image(
                painter = painterResource(
                    id = if (movieView.isFav) R.drawable.fav_plus else R.drawable.plus
                ),
                alpha = 0.8f,
                contentDescription = "plus",
                modifier = Modifier
                    .constrainAs(plus) {
                        start.linkTo(movieCard.start, padding_8)
                        top.linkTo(parent.top)
                    }
            )
            Text(
                text = movieView.title,
                style = TextStyle(
                    color = ColorDarkPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(movieCard.end, padding_8)
                        top.linkTo(parent.top)
                    }
            )
            Text(
                text = movieView.releaseDate,
                style = TextStyle(
                    color = colorDarkTertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .constrainAs(date) {
                        start.linkTo(movieCard.end, padding_8)
                        top.linkTo(title.bottom, padding_4)
                    }
            )
            Text(
                text = movieView.genreIds.map { genres[it] }.joinToString(", "),
                style = TextStyle(
                    color = colorDarkTertiary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier
                    .constrainAs(genresView) {
                        start.linkTo(movieCard.end, padding_8)
                        top.linkTo(date.bottom, padding_4)
                    }
            )
        }
    }
}