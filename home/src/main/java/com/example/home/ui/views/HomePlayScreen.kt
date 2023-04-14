package com.example.home.ui.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.home.ui.viewmodels.HomePlayerViewModel

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun HomePlayScreen(
    viewModel: HomePlayerViewModel,
    goToDetail: (Int) -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {

    val genresDict = viewModel.genresDict.collectAsState(emptyMap())
    val filteredMovies by viewModel.filteredMovieList.collectAsState(emptyList())
    val isLoading = remember { viewModel.isLoading }
    val queryValue: String by viewModel.query.observeAsState(initial = "")

    LaunchedEffect("Load Movies") {
        viewModel.apply{
            loadRemoteData()
        }
    }

}