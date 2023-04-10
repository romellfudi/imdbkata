package com.example.core.view.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag

@Composable
fun AppResourceImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier
){
    Image(
        painter = painterResource(id = imageRes),
        modifier = modifier
            .semantics { testTag = "splash_screen_image" },
        contentScale = ContentScale.Inside,
        contentDescription = "IMDB logo"
    )
}