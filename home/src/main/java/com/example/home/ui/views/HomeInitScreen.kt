package com.example.home.ui.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.home.ui.viewmodels.HomeIntViewModel

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeInitScreen(
    viewModel: HomeIntViewModel,
    toMovieDetail: (Int) -> Unit,
    isDark: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier
) {

    val movieTopRatedList by viewModel.movieTopRatedList.collectAsState(emptyList())
    val moviePopularList by viewModel.moviePopularList.collectAsState(emptyList())
    val isLoading = remember { viewModel.isLoading }

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
            InitView(
                movieTopRatedList,
                moviePopularList,
                toMovieDetail = toMovieDetail,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}

@Composable
fun InitView(
    movieTopRatedList: List<MovieView>,
    moviePopularList: List<MovieView>,
    toMovieDetail: (Int) -> Unit,
    modifier: Modifier
) {
    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        val (mostPopular, popular, topRated) = createRefs()
        if (moviePopularList.isNotEmpty()) {
            MostPopular(
                movieView = moviePopularList.first(),
                modifier = Modifier
                    .constrainAs(mostPopular) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(parent.top)
                    },
                goToDetail = toMovieDetail
            )
            IMDBMovies(
                title = stringResource(R.string.best_movies),
                movies = moviePopularList.subList(1, moviePopularList.size),
                goToDetail = toMovieDetail,
                modifier = Modifier
                    .constrainAs(popular) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(mostPopular.bottom, padding_24)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            )
            IMDBMovies(
                title = stringResource(R.string.favourites_everyone),
                movies = movieTopRatedList,
                goToDetail = toMovieDetail,
                modifier = Modifier
                    .constrainAs(topRated) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = popular.bottom,
                            topMargin = padding_4,
                            bottom = parent.bottom
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            )
        }

    }
}

@Composable
fun MostPopular(
    movieView: MovieView,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .clickable { goToDetail(movieView.id) }) {
        val (movieBackground, moviePoster, movieName, movieDescription) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieView.tileUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(240.dp)
                .constrainAs(movieBackground) {
                    top.linkTo(parent.top)
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                }
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieView.posterUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(140.dp)
                .width(100.dp)
                .constrainAs(moviePoster) {
                    top.linkTo(movieBackground.top, 160.dp)
                    start.linkTo(parent.start, padding_24)
                }
        )

        Text(text = movieView.originalTitle,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
            modifier = Modifier
                .constrainAs(movieName) {
                    top.linkTo(movieBackground.bottom, padding_8)
                    linkTo(
                        start = moviePoster.end,
                        startMargin = padding_16,
                        endMargin = padding_16,
                        end = parent.end
                    )
                    width = Dimension.fillToConstraints
                })

        Text(text = stringResource(R.string.official_trailer),
            style = MaterialTheme.typography.overline,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(movieDescription) {
                    top.linkTo(movieName.bottom, padding_4)
                    linkTo(
                        start = moviePoster.end,
                        startMargin = padding_16,
                        endMargin = padding_16,
                        end = parent.end
                    )
                    width = Dimension.fillToConstraints
                })
    }
}

@Composable
fun IMDBMovies(
    title: String,
    movies: List<MovieView>,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val state = rememberLazyListState()
        val (spacer, textColor, rv) = createRefs()

        Divider(
            modifier = Modifier
                .background(Color5)
                .height(padding_16)
                .constrainAs(spacer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(parent.top)
                }
        )
        ConstraintLayout(modifier = Modifier
            .constrainAs(textColor) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(spacer.bottom)
            }
            .fillMaxWidth()
        ) {
            val (colorLine, movieTitle) = createRefs()
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(padding_6)
                    .height(padding_24)
                    .background(Color1)
                    .constrainAs(colorLine) {
                        start.linkTo(parent.start, padding_24)
                        top.linkTo(parent.top, padding_16)
                    }
            )
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.constrainAs(movieTitle) {
                    linkTo(
                        start = colorLine.end,
                        startMargin = padding_12,
                        end = parent.end,
                        endMargin = padding_24
                    )
                    top.linkTo(colorLine.top)
                    bottom.linkTo(colorLine.bottom)
                    width = Dimension.fillToConstraints
                }
            )
        }
        LazyRow(
            contentPadding = PaddingValues(vertical = padding_16),
            state = state,
            modifier = Modifier.constrainAs(rv) {
                linkTo(
                    start = parent.start,
                    startMargin = padding_16,
                    end = parent.end,
                    endMargin = padding_16
                )
                top.linkTo(textColor.bottom, padding_16)
                width = Dimension.fillToConstraints
            }
        ) {
            items(movies) { movieView ->
                IMDBMoviesItem(
                    movieView,
                    goToDetail = goToDetail
                )
            }
        }
    }
}

@Composable
fun IMDBMoviesItem(
    movieModel: MovieView,
    goToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable { goToDetail(movieModel.id) }
            .padding(horizontal = padding_8)
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (movieCard, plus, star, ranking, title, info) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movieModel.posterUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = "movie poster",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(160.dp)
                    .width(110.dp)
                    .constrainAs(movieCard) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        top.linkTo(parent.top)
                    }
            )
            Image(
                painter = painterResource(
                    id = R.drawable.plus
                ),
                alpha = 0.8f,
                contentDescription = "plus",
                modifier = Modifier
                    .constrainAs(plus) {
                        start.linkTo(movieCard.start, padding_8)
                        top.linkTo(parent.top)
                    }
            )
            Image(
                painter = painterResource(
                    id = R.drawable.star
                ),
                contentDescription = "star",
                modifier = Modifier
                    .constrainAs(star) {
                        start.linkTo(movieCard.start, padding_8)
                        top.linkTo(movieCard.bottom, padding_8)
                    }
            )
            Text(
                text = movieModel.voteAverage.toString(),
                textAlign = TextAlign.Start,
                maxLines = 2,
                fontSize = 10.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(ranking) {
                        linkTo(
                            start = plus.end,
                            startMargin = padding_4,
                            end = movieCard.end
                        )
                        top.linkTo(
                            movieCard.bottom,
                            padding_8
                        )
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = movieModel.title,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(title) {
                        linkTo(
                            start = movieCard.start,
                            startMargin = padding_8,
                            end = movieCard.end
                        )
                        top.linkTo(
                            ranking.bottom,
                            padding_4
                        )
                        width = Dimension.fillToConstraints
                    }
            )

            Image(
                painter = painterResource(
                    id = R.drawable.info
                ),
                contentDescription = "info",
                modifier = Modifier
                    .constrainAs(info) {
                        end.linkTo(movieCard.end, padding_8)
                        top.linkTo(title.bottom, padding_8)
                        bottom.linkTo(parent.bottom, padding_8)
                    }
            )
        }
    }
}