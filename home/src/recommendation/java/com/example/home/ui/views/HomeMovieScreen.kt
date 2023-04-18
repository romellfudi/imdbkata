package com.example.home.ui.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.example.data.models.CastView
import com.example.data.models.MovieDetailResponse
import com.example.home.R
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.MovieDetailView
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.viewmodels.HomeMovieViewModel

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeMovieScreen(
    toMovieDetail: (Int) -> Unit,
    backScreen: () -> Unit,
    viewModel: HomeMovieViewModel,
    id: Int,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val isLoading = remember { viewModel.isLoading }
    val movieDetailView by viewModel.movieDetail.collectAsState(null)

    LaunchedEffect("Load Movie Detail") {
        viewModel.fetchMovieDetail(id)
    }

    when (isLoading.value) {
        is HomeState.Loading -> {
            LoadingAnimation(modifier = Modifier.fillMaxSize())
        }
        is HomeState.Error -> {
            FailedAnimation(modifier = Modifier.fillMaxSize())
        }
        is HomeState.Ready -> {
            ConstraintLayout(
                modifier = modifier.fillMaxWidth()
            ) {
                MovieDetailInfoContent(
                    toMovieDetail = toMovieDetail,
                    movieDetailView = movieDetailView,
                    backScreen = backScreen
                )
            }
        }
    }
}

@Composable
fun MovieDetailInfoContent(
    movieDetailView: MovieDetailView?,
    toMovieDetail: (Int) -> Unit,
    backScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(state)
    ) {
        val (toolbar, header, backdropImage, summary, firstDivider, followButton, secondDivider, cast, recommendation, lastDivider) = createRefs()
        MovieDetailToolbar(
            movieDetailView?.detail?.title ?: stringResource(R.string.movie_title_default),
            modifier = Modifier.constrainAs(toolbar) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            },
            backScreen = backScreen
        )
        movieDetailView?.detail?.let {
            MovieDetailHeader(
                movieDetailView.detail,
                modifier = Modifier.constrainAs(header) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(toolbar.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieDetailView?.detail?.tileUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(196.dp)
                .constrainAs(backdropImage) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(header.bottom, padding_4)
                }
        )
        movieDetailView?.detail?.let {
            MovieDetailSummary(
                it,
                modifier = Modifier.constrainAs(summary) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    top.linkTo(backdropImage.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
            )
        }
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(0.5.dp)
                .constrainAs(firstDivider) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    bottom.linkTo(summary.bottom)
                }
        )
        Button(
            onClick = {

            },
            elevation = buttonNoElevation,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .constrainAs(followButton) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_4,
                        end = parent.end,
                        endMargin = padding_4
                    )
                    top.linkTo(firstDivider.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
        ) {
            Text(
                text = stringResource(R.string.add_to_my_list),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color1,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(vertical = padding_4)
            )
        }

        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(padding_8)
                .constrainAs(secondDivider) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    top.linkTo(followButton.bottom, padding_4)
                }
        )
        if (movieDetailView?.cast?.isNotEmpty() == true) {
            MovieDetailCast(
                cast = movieDetailView.cast,
                modifier = Modifier
                    .constrainAs(cast) {
                        linkTo(
                            start = parent.start,
                            end = parent.end,
                        )
                        top.linkTo(secondDivider.bottom, padding_4)
                        width = Dimension.fillToConstraints
                    }
            )
        }
        if (movieDetailView?.recommendation?.isNotEmpty() == true) {
            IMDBMoviesRecommendations(
                title = stringResource(R.string.recommendations),
                movies = movieDetailView.recommendation,
                goToDetail = { toMovieDetail(it) },
                modifier = Modifier.constrainAs(recommendation) {
                    val previous = when {
                        movieDetailView.cast.isNotEmpty() -> cast.bottom
                        else -> secondDivider.bottom
                    }
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    top.linkTo(previous,padding_4)
                    width = Dimension.fillToConstraints
                }
            )
        }
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(padding_8)
                .constrainAs(lastDivider) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    top.linkTo(recommendation.bottom)
                }
        )
    }
}

@Composable
fun MovieDetailToolbar(
    title: String,
    modifier: Modifier,
    backScreen: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val (back, movieTitle, divider) = createRefs()
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "icon",
            tint = Color2,
            modifier = Modifier
                .clickable { backScreen() }
                .constrainAs(back) {
                    start.linkTo(parent.start, padding_4)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color2,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(movieTitle) {
                linkTo(
                    start = parent.start,
                    startMargin = padding_40,
                    end = parent.end,
                    endMargin = padding_40
                )
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(0.5.dp)
                .constrainAs(divider) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                    )
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
fun MovieDetailHeader(
    movie: MovieDetailResponse,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (barTitle, colorLine, movieTitle, original, idMovie) = createRefs()
        ConstraintLayout(modifier = modifier.constrainAs(barTitle) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }.fillMaxWidth()) {
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
                text = movie.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
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
            Text(
                text = movie.originalTitle + stringResource(R.string.original_title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color3,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(original) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_40,
                        end = parent.end,
                        endMargin = padding_24
                    )
                    top.linkTo(colorLine.bottom)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = stringResource(R.string.id) + movie.id,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color3,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(idMovie) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_40,
                        end = parent.end,
                        endMargin = padding_24
                    )
                    top.linkTo(original.bottom)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Composable
fun MovieDetailSummary(
    movie: MovieDetailResponse,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (poster, genres, star, ranking, summary) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.posterUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(106.dp)
                .width(74.dp)
                .constrainAs(poster) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, padding_16)
                    start.linkTo(parent.start, padding_24)
                }
        )
        val genresText = movie.genres.firstOrNull()?.name
        Text(
            text = genresText.orEmpty(),
            color = Color3,
            fontSize = 10.sp,
            modifier = Modifier
                .border(
                    BorderStroke(0.5.dp, Color3),
                    RoundedCornerShape(4.dp)
                )
                .padding(vertical = padding_6, horizontal = padding_4)
                .constrainAs(genres) {
                    top.linkTo(parent.top)
                    start.linkTo(poster.end, padding_16)
                }
        )
        Image(
            painter = painterResource(
                id = R.drawable.star
            ),
            contentDescription = "star icon",
            modifier = Modifier
                .constrainAs(star) {
                    start.linkTo(genres.end, padding_12)
                    top.linkTo(genres.top)
                    bottom.linkTo(genres.bottom)
                }
        )
        Text(
            text = movie.voteAverage.toString(),
            color = Color3,
            fontSize = 12.sp,
            modifier = Modifier
                .constrainAs(ranking) {
                    start.linkTo(star.end, 2.dp)
                    top.linkTo(genres.top)
                    bottom.linkTo(genres.bottom)
                }
        )
        Text(
            text = movie.overview,
            color = Color2,
            fontSize = 14.sp,
            maxLines = 4,
            fontWeight = FontWeight.Normal,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(summary) {
                    start.linkTo(poster.end, padding_16)
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(genres.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun MovieDetailCast(
    cast: List<CastView>,
    modifier: Modifier
) {
    val state = rememberLazyListState()
    ConstraintLayout(modifier = modifier) {
        val (castcolorLine) = createRefs()
        ConstraintLayout(modifier = modifier.constrainAs(castcolorLine) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }.fillMaxWidth()) {
            val (colorLine, movieTitle, listCast) = createRefs()
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
                text = stringResource(R.string.cast),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color2,
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
            LazyRow(
                contentPadding = PaddingValues(vertical = padding_16),
                state = state,
                modifier = Modifier.constrainAs(listCast) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_16,
                        end = parent.end,
                        endMargin = padding_16
                    )
                    top.linkTo(movieTitle.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
            ) {
                items(cast) {
                    MovieDetailCastItem(it)
                }
            }
        }
    }
}

@Composable
fun MovieDetailCastItem(
    castView: CastView,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = padding_4)
    ) {
        val (poster, name, originalName) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(castView.profileUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = "cast profile poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(106.dp)
                .width(74.dp)
                .constrainAs(poster) {
                    linkTo(start = parent.start, end = parent.end)
                    top.linkTo(parent.top)
                }
        )
        Text(
            text = castView.name,
            maxLines = 1,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            color = Color2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(name) {
                            linkTo(
                                start = parent.start,
                                startMargin = padding_4,
                                end = parent.end,
                                endMargin = padding_4
                            )
                            top.linkTo(poster.bottom, padding_4)
                            width = Dimension.fillToConstraints
                }
        )
        Text(
            text = castView.originalName,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontSize = 10.sp,
            fontWeight = FontWeight.Light,
            color = Color3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(originalName) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_4,
                        end = parent.end,
                        endMargin = padding_4
                    )
                    top.linkTo(name.bottom)
                    bottom.linkTo(parent.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
fun IMDBMoviesRecommendations(
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