package com.example.core.view.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.airbnb.lottie.compose.*
import com.example.core.R

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.imdb_loading))
    val progressLottie by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 4f
    )
    LottieAnimation(
        composition = composition,
        progress = { progressLottie },
        modifier = modifier.testTag("home_screen_loading")
    )
}