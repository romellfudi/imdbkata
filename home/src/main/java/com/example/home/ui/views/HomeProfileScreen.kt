package com.example.home.ui.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import com.example.data.models.CastView
import com.example.data.models.NoteView
import com.example.data.models.castViewList
import com.example.data.models.noteViewMockList
import com.example.home.R
import com.example.home.ui.dataview.*
import com.example.home.ui.viewmodels.HomeProfileViewModel

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeProfileScreen(
    viewModel: HomeProfileViewModel,
    onLogout: () -> Unit,
    isDark: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier,
) {

    val firebaseUser by viewModel.user.collectAsState(null)
    val isLoading = remember { viewModel.isLoading }
    val noteViewList = noteViewMockList
    val followMovieList =
//        listOf<MovieView>()
        movieViewFList
    val recentViewedMovieList =
//        listOf<MovieView>()
        movieViewRVList
    val favouriteCastMovieList =
//        listOf<CastView>()
        castViewList

    LaunchedEffect("Load Profile") {
        viewModel.fetchUser()
    }
    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        val (header, note, followList, recentList, favList, profile) = createRefs()
        ProfileHeader(
            userView = firebaseUser,
            modifier = Modifier.constrainAs(header) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )
        NoteListView(
            noteViewList,
            modifier = Modifier.constrainAs(note) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(header.bottom)
                width = Dimension.fillToConstraints
            }
        )
        FollowList(
            followList = followMovieList,
            modifier = Modifier.constrainAs(followList) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(note.bottom)
                height = Dimension.wrapContent
                width = Dimension.fillToConstraints
            }
        )
        MoviesRecentViewed(
            recentViewedMovieList,
            modifier = Modifier.constrainAs(recentList) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(followList.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
        )
        FavouritePeople(
            favouriteCastMovieList,
            modifier = Modifier.constrainAs(favList) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(recentList.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
        )
        ProfileLogoutOptions(
            logout = { onLogout() },
            modifier = Modifier.constrainAs(profile) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(favList.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.wrapContent
                width = Dimension.fillToConstraints
            }
        )
    }

}

@Composable
fun ProfileHeader(userView: UserView?, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val defaultPhoto = stringResource(R.string.default_profile_url)
        val defaultName = stringResource(R.string.default_profile)
        val (profileImage, profileName, settingsIcon, divider) = createRefs()
        AsyncImage(
            placeholder = painterResource(R.drawable.placeholder),
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    when(userView?.picAvailable() ) {
                        true -> userView.photoUrl
                        else -> defaultPhoto
                    }
                )
                .crossfade(true)
                .build(),
            contentDescription = "movie poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .constrainAs(profileImage) {
                    start.linkTo(parent.start, padding_24)
                    top.linkTo(parent.top, padding_16)
                }
        )
        Text(
            text = userView?.name?.ifEmpty{ defaultName} ?: defaultName,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(profileName) {
                    start.linkTo(profileImage.end, padding_16)
                    end.linkTo(settingsIcon.start, padding_16)
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                    width = Dimension.fillToConstraints
                }
        )
        Image(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier
                .constrainAs(settingsIcon) {
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(profileImage.top)
                    bottom.linkTo(profileImage.bottom)
                }
        )
        Divider(
            modifier = Modifier
                .background(Color5)
                .height(0.5.dp)
                .constrainAs(divider) {
                    linkTo(
                        start = profileImage.start,
                        end = settingsIcon.end,
                    )
                    top.linkTo(profileImage.bottom, padding_16)
                    width = Dimension.fillToConstraints

                }
        )

    }
}

@Composable
fun NoteListView(
    noteViewList: List<NoteView>,
    modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val state = rememberLazyListState()
        val (rv) = createRefs()
        LazyRow(
            contentPadding = PaddingValues(horizontal = padding_16),
            state = state,
            modifier = Modifier.constrainAs(rv) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                top.linkTo(parent.top, padding_16)
                bottom.linkTo(parent.bottom, padding_12)
                width = Dimension.fillToConstraints
            }
        ) {
            items(noteViewList) { noteView ->
                NoteViewItem(noteView)
            }
        }
    }
}

@Composable
fun NoteViewItem(
    noteView: NoteView,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(horizontal = padding_8)
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (background, comment, title, count) = createRefs()
            Box(
                modifier = Modifier
                    .background(Color5)
                    .height(80.dp)
                    .width(114.dp)
                    .constrainAs(background) {
                        linkTo(
                            start = parent.start,
                            startMargin = padding_8,
                            end = parent.end,
                            endMargin = padding_8
                        )
                        top.linkTo(parent.top, padding_8)
                    }
            )
            Text(
                text = noteView.comment,
                fontSize = 10.sp,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(comment) {
                    linkTo(
                        start = background.start,
                        startMargin = padding_8,
                        end = background.end,
                        endMargin = padding_8
                    )
                    linkTo(
                        top = background.top,
                        topMargin = padding_8,
                        bottom = background.bottom,
                        bottomMargin = padding_8
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
            Text(
                text = noteView.title,
                fontSize = 10.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(title) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_8,
                        end = parent.end,
                        endMargin = padding_8
                    )
                    top.linkTo(background.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
            )
            Text(
                text = noteView.count.toString(),
                fontSize = 10.sp,
                color = Color3,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(count) {
                    linkTo(
                        start = parent.start,
                        startMargin = padding_8,
                        end = parent.end,
                        endMargin = padding_8
                    )
                    top.linkTo(title.bottom, padding_4)
                    bottom.linkTo(parent.bottom, padding_4)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Composable
fun FollowList(
    followList: List<MovieView>,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (separator, followTitle, followEmptyText, followEmptyButton) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(separator) {
                    top.linkTo(parent.top)
                }
        )
        ConstraintLayout(modifier = Modifier
            .constrainAs(followTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(separator.bottom)
            }
            .fillMaxWidth()) {
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
                text = stringResource(R.string.following),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = 20.sp,
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
        }
        if (followList.isEmpty()) {
            Text(
                text = stringResource(R.string.creation_list),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.constrainAs(followEmptyText) {
                    start.linkTo(parent.start, padding_24)
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(followTitle.bottom, padding_16)
                    width = Dimension.fillToConstraints
                }
            )
        } else {
            IMDBProfileMovies(
                movies = followList,
                goToDetail = {},
                modifier = Modifier
                    .constrainAs(followEmptyText) {
                        start.linkTo(parent.start, padding_8)
                        end.linkTo(parent.end, padding_8)
                        top.linkTo(followTitle.bottom, padding_4)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            )
        }
    }
}

@Composable
fun MoviesRecentViewed(
    recentViewedList: List<MovieView>,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (recent, recentViewedTitle, viewText) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(padding_12)
                .constrainAs(recent) {
                    top.linkTo(parent.top)
                }
        )
        ConstraintLayout(modifier = Modifier
            .constrainAs(recentViewedTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(recent.bottom)
            }
            .fillMaxWidth()) {
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
                text = stringResource(R.string.recentViewed),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = 20.sp,
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
        }
        if (recentViewedList.isEmpty()) {
            Text(
                text = stringResource(R.string.had_not_seen_recently),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.constrainAs(viewText) {
                    start.linkTo(parent.start, padding_24)
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(recentViewedTitle.bottom, padding_16)
                    bottom.linkTo(parent.bottom, padding_24)
                    width = Dimension.fillToConstraints
                }
            )
        } else {
            IMDBProfileMovies(
                movies = recentViewedList,
                goToDetail = {},
                modifier = Modifier
                    .constrainAs(viewText) {
                        start.linkTo(parent.start, padding_8)
                        end.linkTo(parent.end, padding_8)
                        top.linkTo(recentViewedTitle.bottom, padding_4)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
            )
        }
    }
}


@Composable
fun FavouritePeople(
    castList: List<CastView>,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (separator, followTitle) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(padding_8)
                .constrainAs(separator) {
                    top.linkTo(parent.top)
                }
        )
        ConstraintLayout(modifier = modifier
            .constrainAs(followTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(separator.bottom)
            }
            .fillMaxWidth()) {
            val (colorLine, movieTitle, bodyRef, buttonRef) = createRefs()
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
                text = stringResource(R.string.favourite_actors),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = 20.sp,
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
            if (castList.isEmpty()){
                Text(
                    text = stringResource(R.string.add_actors_movies),
                    fontSize = 14.sp,
                    color = Color2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(bodyRef) {
                        start.linkTo(parent.start, padding_24)
                        end.linkTo(parent.end, padding_24)
                        top.linkTo(movieTitle.bottom)
                        width = Dimension.fillToConstraints
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
                    .constrainAs(buttonRef) {
                        linkTo(
                            start = parent.start,
                            startMargin = padding_8,
                            end = parent.end,
                            endMargin = padding_8
                        )
                        top.linkTo(bodyRef.bottom)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            ) {
                Text(
                    text = stringResource(R.string.add_favourite_actors),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color1,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(vertical = padding_16)
                )
            }
            } else {
                IMDBProfileCast(
                    cast = castList,
                    modifier = Modifier
                        .constrainAs(bodyRef) {
                            start.linkTo(parent.start, padding_8)
                            end.linkTo(parent.end, padding_8)
                            top.linkTo(movieTitle.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                        }
                )
            }
        }
    }
}

@Composable
fun IMDBProfileCast(
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
            val (listCast) = createRefs()
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
                    top.linkTo(parent.top)
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
fun ProfileLogoutOptions(
    modifier: Modifier,
    logout: () -> Unit
) {
    ConstraintLayout(modifier = modifier
        .clickable { logout() }
    ) {
        val (separator, button, icon, secondSeparator) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(padding_12)
                .constrainAs(separator) {
                    top.linkTo(parent.top)
                }
        )
        Text(text = stringResource(R.string.log_out),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = padding_24, vertical = padding_16)
                .constrainAs(button) {
                    top.linkTo(separator.bottom)
                    linkTo(start = parent.start, end = parent.end)
                    width = Dimension.fillToConstraints
                }
        )
        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = "icon",
            tint = SeparatorColor,
            modifier = Modifier
                .constrainAs(icon) {
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(button.top)
                    bottom.linkTo(button.bottom)
                }
        )
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(secondSeparator) {
                    top.linkTo(button.bottom)
                }
        )
    }
}

@Composable
fun IMDBProfileMovies(
    movies: List<MovieView>,
    goToDetail: (Int) -> Unit,
    modifier: Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val state = rememberLazyListState()
        val (rv) = createRefs()
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
                top.linkTo(parent.top)
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
