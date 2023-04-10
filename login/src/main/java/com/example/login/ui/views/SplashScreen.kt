package com.example.login.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.view.Color1
import com.example.core.view.compose.AppResourceImage
import com.example.login.R
import com.example.login.ui.viewmodels.SplashViewModel


/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    nextLoginScreen: () -> Unit,
    nextHomeScreen: () -> Unit,
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.timeFinished.collect { isLogged ->
                when (isLogged) {
                    true -> nextHomeScreen()
                    false -> nextLoginScreen()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color1)
    ) {
        AppResourceImage(
            R.drawable.imdb,
            Modifier
                .align(Alignment.Center)
                .requiredSize(200.dp)
        )
    }
}