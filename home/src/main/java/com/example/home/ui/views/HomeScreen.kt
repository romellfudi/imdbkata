package com.example.home.ui.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
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

    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val lifecycle = LocalLifecycleOwner.current.lifecycle
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

    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        modifier = modifier
    ) { paddingValues ->
        BottomNavGraph(
            navController = navController,
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
        val (movie, search, ticket, profile,b) = createRefs()
        TabView(
            screen = TapViewState.Init,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier
                .constrainAs(movie) {
                    start.linkTo(parent.start, spacing_10)
                    end.linkTo(search.start)
                    linkTo(
                        top = parent.top,
                        topMargin = spacing_15,
                        bottomMargin = spacing_15,
                        bottom = parent.bottom
                    )
                }
        )
        TabView(
            screen = TapViewState.Search,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(search) {
                start.linkTo(movie.end, spacing_10)
                end.linkTo(ticket.start)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_15,
                    bottomMargin = spacing_15,
                    bottom = parent.bottom
                )
            }
        )
        TabView(
            screen = TapViewState.Play,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(ticket) {
                start.linkTo(search.end, spacing_10)
                end.linkTo(profile.start)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_15,
                    bottomMargin = spacing_15,
                    bottom = parent.bottom
                )
            }
        )
        TabView(
            screen = TapViewState.Profile,
            currentDestination = currentDestination,
            navController = navController,
            modifier = Modifier.constrainAs(profile) {
                start.linkTo(ticket.end)
                end.linkTo(parent.end, spacing_10)
                linkTo(
                    top = parent.top,
                    topMargin = spacing_15,
                    bottomMargin = spacing_15,
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

    val contentColor =
        if (selected) Color.Black else Color.Black.copy(alpha = 0.3f)

    ConstraintLayout(
        modifier = modifier
            .height(spacing_12)
            .clip(CircleShape)
            .padding(
                start = spacing_10,
                end = spacing_10,
                top = spacing_10,
                bottom = spacing_10
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
                text = screen.title,
                color = contentColor
            )
        }
    }
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
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
            HomeInitScreen(viewModel = initViewModel)
        }
        composable(route = TapViewState.Search.route) {
            HomeSearchScreen(viewModel = searchViewModel)
        }

        composable(route = TapViewState.Play.route) {
            HomePlayScreen(viewModel = playerViewModel)
        }
        composable(route = TapViewState.Profile.route) {
            HomeProfileScreen(viewModel = profileViewModel)
        }
    }
}
