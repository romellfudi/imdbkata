package com.example.imdbkata.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.view.DarkThemeColors
import com.example.core.view.ThemeColors
import com.example.home.ui.viewmodels.*
import com.example.home.ui.views.HomeMovieScreen
import com.example.home.ui.views.HomeScreen
import com.example.login.ui.viewmodels.LoginViewModel
import com.example.login.ui.viewmodels.RegisterViewModel
import com.example.login.ui.viewmodels.SplashViewModel
import com.example.login.ui.views.LoginScreen
import com.example.login.ui.views.RegisterScreen
import com.example.login.ui.views.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val initViewModel: HomeIntViewModel by viewModels()
    private val searchViewModel: HomeSearchViewModel by viewModels()
    private val playerViewModel: HomePlayerViewModel by viewModels()
    private val profileViewModel: HomeProfileViewModel by viewModels()
    private val homeMovieViewModel: HomeMovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) DarkThemeColors else ThemeColors
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navigationController by rememberUpdatedState(newValue = rememberNavController())
                    NavHost(navController = navigationController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(
                                viewModel = splashViewModel,
                                nextLoginScreen = {
                                    navigationController.navigateAndReplaceStartRoute("login")
                                },
                                nextHomeScreen = {
                                    navigationController.navigateAndReplaceStartRoute("home")
                                }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                loginViewModel,
                                registerScreen = {
                                    navigationController.navigateAndReplaceStartRoute("register")
                                },
                                homeScreen = {
                                    navigationController.navigateAndReplaceStartRoute("home")
                                    homeViewModel.cleanGuestData()
                                },
                                displayMessage = ::showTextInToast
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                registerViewModel,
                                backScreen = {
                                    navigationController.navigateAndReplaceStartRoute("login")
                                },
                                nextScreen = {
                                    navigationController.navigateAndReplaceStartRoute("home")
                                },
                                displayMessage = ::showTextInToast
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                toInitView = { navigationController.navigateAndReplaceStartRoute("home") },
                                toMovieDetail = { navigationController.navigate("detail/$it") },
                                backScreen = {
                                    navigationController.navigateAndReplaceStartRoute("login")
                                },
                                homeViewModel,
                                initViewModel,
                                searchViewModel,
                                playerViewModel,
                                profileViewModel,
                            )
                        }
                        composable(
                            route = "detail/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                }
                            ),
                            content = { backStackEntry ->
                                HomeMovieScreen(
                                    toMovieDetail = { navigationController.navigate("detail/$it") },
                                    backScreen = {
                                        navigationController.popBackStack()
                                    },
                                    viewModel = homeMovieViewModel,
                                    id = backStackEntry.arguments?.getInt("id") ?: 0
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun showTextInToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}

fun NavHostController.navigateAndReplaceStartRoute(newHomeRoute: String) {
    navigate(newHomeRoute) {
        popUpTo(this@navigateAndReplaceStartRoute.graph.id) {
            inclusive = true
        }
    }
}