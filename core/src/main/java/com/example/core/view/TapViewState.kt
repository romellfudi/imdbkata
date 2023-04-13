package com.example.core.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TapViewState(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Init : TapViewState(
        route = "init",
        title = "Inicio",
        icon = Icons.Filled.Home
    )

    object Search : TapViewState(
        route = "search",
        title = "Buscar",
        icon = Icons.Filled.Search
    )

    object Play : TapViewState(
        route = "play",
        title = "Play",
        icon = Icons.Filled.PlayCircle
    )

    object Profile : TapViewState(
        route = "profile",
        title = "Perfil",
        icon = Icons.Filled.AccountCircle
    )
}
