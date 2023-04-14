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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.core.view.*
import com.example.data.models.NoteModel
import com.example.data.models.noteModelList
import com.example.home.R
import com.example.home.ui.dataview.MovieView
import com.example.home.ui.dataview.UserView
import com.example.home.ui.viewmodels.HomeProfileViewModel

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeProfileScreen(
    viewModel: HomeProfileViewModel,
    onSignOut: () -> Unit,
    isDark: Boolean = isSystemInDarkTheme(),
    modifier: Modifier = Modifier,
) {

    val firebaseUser by viewModel.user.collectAsState(null)
    val isLoading = remember { viewModel.isLoading }

    LaunchedEffect("Load Profile") {
        viewModel.fetchUser()
    }
    val state = rememberScrollState()
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state)
    ) {
        val (header, note, followList, recent, favourite, profile) = createRefs()
        ProfileHeader(
            userView = firebaseUser,
            modifier = Modifier.constrainAs(header) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
            }
        )
        NoteListView(
            modifier = Modifier.constrainAs(note) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(header.bottom)
                width = Dimension.fillToConstraints
            }
        )
        FollowList(
            listOf(),
            modifier = Modifier.constrainAs(followList) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(note.bottom)
                width = Dimension.fillToConstraints
            }
        )
        MoviesRecentViewed(
            listOf(),
            modifier = Modifier.constrainAs(recent) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(followList.bottom)
                width = Dimension.fillToConstraints
            }
        )
        FavouritePeople(
            modifier = Modifier.constrainAs(favourite) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(recent.bottom)
                width = Dimension.fillToConstraints
            }
        )
        ProfileOptions(
            signOut = { onSignOut() },
            modifier = Modifier.constrainAs(profile) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(favourite.bottom)
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
                    userView?.photoUrl?.ifEmpty { defaultPhoto } ?: defaultPhoto
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
            fontWeight = FontWeight.Medium,
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
fun NoteListView(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val noteModelList = noteModelList
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
            items(noteModelList) { noteModel ->
                NoteViewItem(noteModel)
            }
        }
    }
}

@Composable
fun NoteViewItem(
    noteModel: NoteModel,
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
                text = noteModel.comment,
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
                text = noteModel.title,
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
                text = noteModel.count.toString(),
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
        // title 1
        ConstraintLayout(modifier = Modifier
            .constrainAs(followTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(separator.bottom)
            }
            .fillMaxWidth()) {
            val (bullet, titleRef) = createRefs()
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(padding_6)
                    .height(padding_24)
                    .background(Color1)
                    .constrainAs(bullet) {
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
                fontWeight = FontWeight.Medium,
                modifier = Modifier.constrainAs(titleRef) {
                    linkTo(
                        start = bullet.end,
                        startMargin = padding_12,
                        end = parent.end,
                        endMargin = padding_24
                    )
                    top.linkTo(bullet.top)
                    bottom.linkTo(bullet.bottom)
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
//            Button(
//                onClick = {
//
//                },
//                elevation = buttonNoElevation,
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color.Transparent,
//                    disabledBackgroundColor = Color.Transparent
//                ),
//                modifier = Modifier
//                    .constrainAs(followEmptyButton) {
//                        linkTo(
//                            start = parent.start,
//                            startMargin = padding_8,
//                            end = parent.end,
//                            endMargin = padding_8
//                        )
//                        top.linkTo(followEmptyText.bottom, padding_24)
//                        bottom.linkTo(parent.bottom, padding_24)
//                        width = Dimension.fillToConstraints
//                    }
//            ) {
//                Text(
//                    text = stringResource(R.string.start_marking_list),
//                    textAlign = TextAlign.Center,
//                    fontSize = 18.sp,
//                    color = Color.White,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(
//                            color = Color1,
//                            shape = MaterialTheme.shapes.medium
//                        )
//                        .padding(
//                            vertical = padding_16
//                        )
//                )
//            }
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
                .height(12.dp)
                .constrainAs(recent) {
                    top.linkTo(parent.top)
                }
        )
        // title 2
        ConstraintLayout(modifier = Modifier
            .constrainAs(recentViewedTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(recent.bottom)
            }
            .fillMaxWidth()) {
            val (bullet, titleRef) = createRefs()
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(padding_6)
                    .height(padding_24)
                    .background(Color1)
                    .constrainAs(bullet) {
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
                fontWeight = FontWeight.Medium,
                modifier = Modifier.constrainAs(titleRef) {
                    linkTo(
                        start = bullet.end,
                        startMargin = padding_12,
                        end = parent.end,
                        endMargin = padding_24
                    )
                    top.linkTo(bullet.top)
                    bottom.linkTo(bullet.bottom)
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
        }
    }
}


@Composable
fun FavouritePeople(
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
        // title 3
        ConstraintLayout(modifier = modifier
            .constrainAs(followTitle) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(separator.bottom)
            }
            .fillMaxWidth()) {
            val (bullet, titleRef, bodyRef, buttonRef) = createRefs()
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(padding_6)
                    .height(padding_24)
                    .background(Color1)
                    .constrainAs(bullet) {
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
                fontWeight = FontWeight.Medium,
                modifier = Modifier.constrainAs(titleRef) {
                    linkTo(
                        start = bullet.end,
                        startMargin = padding_12,
                        end = parent.end,
                        endMargin = padding_24
                    )
                    top.linkTo(bullet.top)
                    bottom.linkTo(bullet.bottom)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = stringResource(R.string.add_actors_movies),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.constrainAs(bodyRef) {
                    start.linkTo(parent.start, padding_24)
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(titleRef.bottom)
                    width = Dimension.fillToConstraints
                }
            )
//            Button(
//                onClick = {
//
//                },
//                elevation = buttonNoElevation,
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = Color.Transparent,
//                    disabledBackgroundColor = Color.Transparent
//                ),
//                modifier = Modifier
//                    .constrainAs(buttonRef) {
//                        linkTo(
//                            start = parent.start,
//                            startMargin = padding_8,
//                            end = parent.end,
//                            endMargin = padding_8
//                        )
//                        top.linkTo(bodyRef.bottom, padding_24)
//                        bottom.linkTo(parent.bottom, padding_24)
//                        width = Dimension.fillToConstraints
//                    }
//            ) {
//                Text(
//                    text = stringResource(R.string.add_favourite_actors),
//                    textAlign = TextAlign.Center,
//                    fontSize = 18.sp,
//                    color = Color.White,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(
//                            color = Color1,
//                            shape = MaterialTheme.shapes.medium
//                        )
//                        .padding(
//                            vertical = padding_16
//                        )
//                )
//            }
        }
    }
}

@Composable
fun ProfileOptions(
    modifier: Modifier,
    signOut: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (separator, signout, signoutIcon, secondSeparator) = createRefs()
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
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
                .clickable { signOut() }
                .constrainAs(signout) {
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
                .constrainAs(signoutIcon) {
                    end.linkTo(parent.end, padding_24)
                    top.linkTo(signout.top)
                    bottom.linkTo(signout.bottom)
                }
        )
        Divider(
            modifier = Modifier
                .background(SeparatorColor)
                .height(12.dp)
                .constrainAs(secondSeparator) {
                    top.linkTo(signout.bottom)
                }
        )
    }
}
