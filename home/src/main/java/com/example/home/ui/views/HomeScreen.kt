package com.example.home.ui.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
import com.example.core.view.*
import com.example.home.R
import com.example.home.ui.viewmodels.*

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomeScreen(
    backScreen: () -> Unit,
    viewModel: HomeViewModel,
    initViewModel: HomeIntViewModel,
    searchViewModel: HomeSearchViewModel,
    playerViewModel: HomePlayerViewModel,
    profileViewModel: HomeProfileViewModel,
    modifier: Modifier = Modifier
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val navController = rememberNavController()

    LaunchedEffect("Clear search view") {
        searchViewModel.search()
    }

    OnBackDispatcherCompose(
        backScreen = backScreen,
        viewModel = viewModel,
        backPressDispatcher = backPressDispatcher,
        lifecycle = lifecycle
    )

    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        modifier = modifier
    ) { paddingValues ->
        OptionsNavHost(
            navController = navController,
            viewModel = viewModel,
            initViewModel = initViewModel,
            searchViewModel = searchViewModel,
            playerViewModel = playerViewModel,
            profileViewModel = profileViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        )
    }

}


@Composable
fun OnBackDispatcherCompose(
    backScreen: () -> Unit,
    viewModel: HomeViewModel,
    backPressDispatcher: OnBackPressedDispatcher?,
    lifecycle: Lifecycle
) {
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.shouldGoToLogin.collect {
                if (it) {
                    backScreen()
                }
            }
        }
    }
    val callback = remember(backScreen) {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.signOut()
            }
        }
    }

    DisposableEffect(key1 = backPressDispatcher) {
        callback.isEnabled = true
        backPressDispatcher?.addCallback(callback)
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                callback.remove()
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            callback.remove()
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    ConstraintLayout(
        modifier = modifier
            .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color1)
            .fillMaxWidth()
    ) {
        val (movie, search, player, profile, b) = createRefs()
        TabView(
            screen = TapViewState.Init,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier
                .constrainAs(movie) {
                    start.linkTo(parent.start, no_padding)
                    end.linkTo(search.start)
                    linkTo(
                        top = parent.top,
                        topMargin = padding_15,
                        bottomMargin = padding_15,
                        bottom = parent.bottom
                    )
                }
        )
        TabView(
            screen = TapViewState.Search,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(search) {
                start.linkTo(movie.end, no_padding)
                end.linkTo(player.start)
                linkTo(
                    top = parent.top,
                    topMargin = padding_15,
                    bottomMargin = padding_15,
                    bottom = parent.bottom
                )
            }
        )
        TabView(
            screen = TapViewState.Play,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(player) {
                start.linkTo(search.end, no_padding)
                end.linkTo(profile.start)
                linkTo(
                    top = parent.top,
                    topMargin = padding_15,
                    bottomMargin = padding_15,
                    bottom = parent.bottom
                )
            }
        )
        TabView(
            screen = TapViewState.Profile,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(profile) {
                start.linkTo(player.end)
                end.linkTo(parent.end, no_padding)
                linkTo(
                    top = parent.top,
                    topMargin = padding_15,
                    bottomMargin = padding_15,
                    bottom = parent.bottom
                )
            }
        )
    }
}

@Composable
fun TabView(
    screen: TapViewState,
    currentDestination: NavDestination?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val contentColor = if (selected) Color2 else Color2.copy(alpha = 0.3f)

    ConstraintLayout(
        modifier = modifier
            .height(padding_60)
            .clip(CircleShape)
            .padding(
                start = no_padding,
                end = no_padding,
                top = no_padding,
                bottom = no_padding
            )
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        val (icon, text) = createRefs()
        Icon(
            imageVector = screen.icon,
            contentDescription = "icon",
            tint = contentColor,
            modifier = Modifier.constrainAs(icon) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                top.linkTo(parent.top)
            }
        )
        AnimatedVisibility(
            visible = selected,
            modifier = Modifier.constrainAs(text) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                linkTo(
                    top = icon.bottom,
                    bottom = parent.bottom
                )
            }
        ) {
            Text(
                text = when (screen) {
                    TapViewState.Init -> stringResource(R.string.home)
                    TapViewState.Search -> stringResource(R.string.search)
                    TapViewState.Play -> stringResource(R.string.play)
                    TapViewState.Profile -> stringResource(R.string.profile)
                },
                color = contentColor
            )
        }
    }
}

@Composable
fun OptionsNavHost(
    navController: NavHostController,
    viewModel: HomeViewModel,
    initViewModel: HomeIntViewModel,
    searchViewModel: HomeSearchViewModel,
    playerViewModel: HomePlayerViewModel,
    profileViewModel: HomeProfileViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TapViewState.Init.route,
        modifier = modifier
    ) {
        composable(route = TapViewState.Init.route) {
            HomeInitScreen(initViewModel, viewModel::onMovieClicked)
        }
        composable(route = TapViewState.Search.route) {
            HomeSearchScreen(searchViewModel, viewModel::onMovieClicked)
        }
        composable(route = TapViewState.Play.route) {
            HomePlayScreen(playerViewModel, viewModel::onMovieClicked)
        }
        composable(route = TapViewState.Profile.route) {
            HomeProfileScreen(profileViewModel, viewModel::signOut)
        }
    }
}
