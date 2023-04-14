package com.example.home.ui.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.view.*
import com.example.core.view.compose.FailedAnimation
import com.example.core.view.compose.LoadingAnimation
import com.example.home.R
import com.example.home.helpers.HomeState
import com.example.home.ui.viewmodels.*

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeMovieScreen(
    viewModel: HomeMovieViewModel,
    id: Int,
    modifier: Modifier = Modifier
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val isLoading = remember { viewModel.isLoading }
    val movie by viewModel.movieDetail.observeAsState()
    val recommendations by viewModel.recommendations.observeAsState(emptyList())

    LaunchedEffect("Load Movie Detail") {
        viewModel.fetchMovieDetail(id)
    }

    when(isLoading.value) {
        is HomeState.Loading -> {
            LoadingAnimation(modifier = Modifier.fillMaxSize())
        }
        is HomeState.Error -> {
            FailedAnimation(modifier = Modifier.fillMaxSize())
        }
        is HomeState.Ready -> {
//    ConstraintLayout(
//        modifier = modifier.fillMaxWidth()
//    ) {
//        val (loading, content) = createRefs()
//        if (showLoading(uiState)) {
//            Loading(
//                modifier = Modifier.constrainAs(
//                    loading
//                ) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end
//                    )
//                    linkTo(
//                        top = parent.top,
//                        bottom = parent.bottom
//                    )
//                }
//            )
//        } else {
//            getDataFromUiState(uiState)?.let { detailScreenModel ->
//                MovieDetailContent(
//                    detailScreenModel = detailScreenModel,
//                    modifier = Modifier.constrainAs(content) {
//                        linkTo(
//                            start = parent.start,
//                            end = parent.end
//                        )
//                        linkTo(
//                            top = parent.top,
//                            bottom = parent.bottom
//                        )
//                    }
//                )
//            }
//        }
//    }
        }

    }


}

//@Composable
//fun MovieDetailContent(
//    detailScreenModel: DetailScreenModel,
//    modifier: Modifier = Modifier
//) {
//    val state = rememberScrollState()
//    ConstraintLayout(
//        modifier = modifier
//            .fillMaxWidth()
//            .verticalScroll(state)
//    ) {
//        val (toolbar, header, backdropImage, summary, firstDivider, followButton, secondDivider, cast, recommendation) = createRefs()
//        MovieDetailToolbar(
//            detailScreenModel.detail.title,
//            modifier = Modifier.constrainAs(toolbar) {
//                linkTo(
//                    start = parent.start,
//                    end = parent.end
//                )
//                top.linkTo(parent.top)
//                width = Dimension.fillToConstraints
//            }
//        )
//        MovieDetailHeader(
//            detailScreenModel.detail,
//            modifier = Modifier.constrainAs(header) {
//                linkTo(
//                    start = parent.start,
//                    end = parent.end
//                )
//                top.linkTo(toolbar.bottom)
//                width = Dimension.fillToConstraints
//            }
//        )
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(detailScreenModel.detail.getBackgroundPath())
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.placeholder),
//            contentDescription = "movie poster",
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier
//                .height(196.dp)
//                .constrainAs(backdropImage) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end
//                    )
//                    top.linkTo(header.bottom, padding_4)
//                }
//        )
//        MovieDetailSummary(
//            detailScreenModel.detail,
//            modifier = Modifier.constrainAs(summary) {
//                linkTo(
//                    start = parent.start,
//                    end = parent.end
//                )
//                top.linkTo(backdropImage.bottom, padding_4)
//                width = Dimension.fillToConstraints
//            }
//        )
//        Divider(
//            modifier = Modifier
//                .background(SeparatorColor)
//                .height(0.5.dp)
//                .constrainAs(firstDivider) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end,
//                    )
//                    bottom.linkTo(summary.bottom)
//                }
//        )
//        Button(
//            onClick = {
//
//            },
//            elevation = buttonNoElevation,
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color.Transparent,
//                disabledBackgroundColor = Color.Transparent
//            ),
//            modifier = Modifier
//                .constrainAs(followButton) {
//                    linkTo(
//                        start = parent.start,
//                        startMargin = padding_4,
//                        end = parent.end,
//                        endMargin = padding_4
//                    )
//                    top.linkTo(firstDivider.bottom, padding_4)
//                    width = Dimension.fillToConstraints
//                }
//        ) {
//            Text(
//                text = "Agregar a mi lista de seguimiento",
//                textAlign = TextAlign.Center,
//                fontSize = 18.sp,
//                color = Color.White,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        color = PrimaryColor,
//                        shape = MaterialTheme.shapes.medium
//                    )
//                    .padding(
//                        vertical = spacing_4
//                    )
//            )
//        }
//
//        Divider(
//            modifier = Modifier
//                .background(SeparatorColor)
//                .height(0.5.dp)
//                .constrainAs(secondDivider) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end,
//                    )
//                    top.linkTo(followButton.bottom, padding_4)
//                }
//        )
//        MovieDetailCast(
//            cast = detailScreenModel.detail.castList,
//            modifier = Modifier
//                .constrainAs(cast) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end,
//                    )
//                    top.linkTo(secondDivider.bottom, padding_4)
//                    width = Dimension.fillToConstraints
//                }
//        )
//        if (detailScreenModel.recommendation.isNotEmpty()) {
//            IMDBMovies(
//                title = "Recomendados",
//                movies = detailScreenModel.recommendation,
//                goToDetail = {
//
//                },
//                modifier = Modifier.constrainAs(recommendation) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end,
//                    )
//                    top.linkTo(cast.bottom, padding_4)
//                    width = Dimension.fillToConstraints
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun MovieDetailToolbar(
//    title: String,
//    modifier: Modifier,
//    goBack: () -> Unit
//) {
//    ConstraintLayout(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(48.dp)
//    ) {
//        val (back, titleRef, divider) = createRefs()
//        Icon(
//            imageVector = Icons.Filled.ArrowBack,
//            contentDescription = "icon",
//            tint = Color.Black,
//            modifier = Modifier
//                .clickable { goBack() }
//                .constrainAs(back) {
//                    start.linkTo(parent.start, spacing_4)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                }
//        )
//        Text(
//            text = title,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            color = Color.Black,
//            fontWeight = FontWeight.Medium,
//            modifier = Modifier.constrainAs(titleRef) {
//                linkTo(
//                    start = parent.start,
//                    startMargin = spacing_10,
//                    end = parent.end,
//                    endMargin = spacing_10
//                )
//                top.linkTo(parent.top)
//                bottom.linkTo(parent.bottom)
//            }
//        )
//        Divider(
//            modifier = Modifier
//                .background(SeparatorColor)
//                .height(0.5.dp)
//                .constrainAs(divider) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end,
//                    )
//                    bottom.linkTo(parent.bottom)
//                }
//        )
//    }
//}
//
//@Composable
//fun MovieDetailHeader(
//    movie: MovieModel,
//    modifier: Modifier
//) {
//    ConstraintLayout(modifier = modifier) {
//        val (titleBullet, originalTitle, id) = createRefs()
//        IMDBTitle(
//            title = movie.title,
//            modifier = Modifier
//                .constrainAs(titleBullet) {
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    top.linkTo(parent.top)
//                }
//        )
//        Text(
//            text = "${movie.originalTitle} (titulo original)",
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            color = TextDetailColor,
//            fontSize = 10.sp,
//            modifier = Modifier.constrainAs(originalTitle) {
//                linkTo(
//                    start = parent.start,
//                    startMargin = spacing_10,
//                    end = parent.end,
//                    endMargin = spacing_6
//                )
//                top.linkTo(titleBullet.bottom, padding_4)
//                width = Dimension.fillToConstraints
//            }
//        )
//        Text(
//            text = "ID: ${movie.id}",
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            color = TextDetailColor,
//            fontSize = 12.sp,
//            modifier = Modifier.constrainAs(id) {
//                linkTo(
//                    start = parent.start,
//                    startMargin = spacing_10,
//                    end = parent.end,
//                    endMargin = spacing_6
//                )
//                top.linkTo(originalTitle.bottom, spacing_1)
//                width = Dimension.fillToConstraints
//            }
//        )
//    }
//}
//
//@Composable
//fun MovieDetailSummary(
//    movie: MovieModel,
//    modifier: Modifier
//) {
//    ConstraintLayout(modifier = modifier) {
//        val (poster, genres, star, ranking, summary) = createRefs()
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(movie.getImagePath())
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.placeholder),
//            contentDescription = "movie poster",
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier
//                .height(106.dp)
//                .width(74.dp)
//                .constrainAs(poster) {
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom, spacing_4_2)
//                    start.linkTo(parent.start, spacing_6)
//                }
//        )
//        val genresText = movie.genres.firstOrNull()?.name
//        Text(
//            text = genresText.orEmpty(),
//            color = TextDetailColor,
//            fontSize = 10.sp,
//            modifier = Modifier
//                .border(
//                    BorderStroke(0.5.dp, TextDetailColor),
//                    RoundedCornerShape(4.dp)
//                )
//                .padding(vertical = spacing_1_2, horizontal = padding_4)
//                .constrainAs(genres) {
//                    top.linkTo(parent.top)
//                    start.linkTo(poster.end, spacing_4)
//                }
//        )
//        Image(
//            painter = painterResource(
//                id = R.drawable.ic_star_12_12
//            ),
//            contentDescription = "star",
//            modifier = Modifier
//                .constrainAs(star) {
//                    start.linkTo(genres.end, spacing_3)
//                    top.linkTo(genres.top)
//                    bottom.linkTo(genres.bottom)
//                }
//        )
//        Text(
//            text = movie.voteAverage.toString(),
//            color = TextDetailColor,
//            fontSize = 12.sp,
//            modifier = Modifier
//                .constrainAs(ranking) {
//                    start.linkTo(star.end, 2.dp)
//                    top.linkTo(genres.top)
//                    bottom.linkTo(genres.bottom)
//                }
//        )
//        Text(
//            text = movie.overview.orEmpty(),
//            color = Color.Black,
//            fontSize = 14.sp,
//            maxLines = 4,
//            fontWeight = FontWeight.Normal,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier
//                .constrainAs(summary) {
//                    start.linkTo(poster.end, spacing_4)
//                    end.linkTo(parent.end, spacing_6)
//                    top.linkTo(genres.bottom, spacing_1)
//                    width = Dimension.fillToConstraints
//                }
//        )
//    }
//}
//
//@Composable
//fun MovieDetailCast(
//    cast: List<CastModel>,
//    modifier: Modifier
//) {
//    val state = rememberLazyListState()
//    ConstraintLayout(modifier = modifier) {
//        val (castBullet, rv) = createRefs()
//        IMDBTitle(
//            title = "Reparto",
//            modifier = Modifier
//                .constrainAs(castBullet) {
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    top.linkTo(parent.top)
//                }
//        )
//        LazyRow(
//            contentPadding = PaddingValues(vertical = spacing_4),
//            state = state,
//            modifier = Modifier.constrainAs(rv) {
//                linkTo(
//                    start = parent.start,
//                    startMargin = spacing_4,
//                    end = parent.end,
//                    endMargin = spacing_4
//                )
//                top.linkTo(castBullet.bottom, padding_4)
//                width = Dimension.fillToConstraints
//            }
//        ) {
//            items(cast) { castModel ->
//                MovieDetailCastItem(
//                    castModel
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun MovieDetailCastItem(
//    castModel: CastModel,
//    modifier: Modifier = Modifier
//) {
//    ConstraintLayout(
//        modifier = modifier
//            .padding(horizontal = padding_4)
//    ) {
//        val (poster, name, originalName) = createRefs()
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(castModel.getImagePath())
//                .crossfade(true)
//                .build(),
//            placeholder = painterResource(R.drawable.placeholder),
//            contentDescription = "cast poster",
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier
//                .height(106.dp)
//                .width(74.dp)
//                .constrainAs(poster) {
//                    linkTo(
//                        start = parent.start,
//                        end = parent.end
//                    )
//                    top.linkTo(parent.top)
//                }
//        )
//        Text(
//            text = castModel.name,
//            maxLines = 1,
//            fontSize = 10.sp,
//            textAlign = TextAlign.Center,
//            fontWeight = FontWeight.Light,
//            color = Color.Black,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier
//                .constrainAs(name) {
//                    linkTo(
//                        start = parent.start,
//                        startMargin = spacing_1,
//                        end = parent.end,
//                        endMargin = spacing_1
//                    )
//                    top.linkTo(
//                        poster.bottom,
//                        spacing_1
//                    )
//                    width = Dimension.fillToConstraints
//                }
//        )
//        Text(
//            text = castModel.originalName,
//            textAlign = TextAlign.Center,
//            maxLines = 1,
//            fontSize = 10.sp,
//            fontWeight = FontWeight.Light,
//            color = TextDetailColor,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier
//                .constrainAs(originalName) {
//                    linkTo(
//                        start = parent.start,
//                        startMargin = spacing_1,
//                        end = parent.end,
//                        endMargin = spacing_1
//                    )
//                    top.linkTo(
//                        name.bottom
//                    )
//                    bottom.linkTo(
//                        parent.bottom,
//                        spacing_1
//                    )
//                    width = Dimension.fillToConstraints
//                }
//        )
//    }
//}
