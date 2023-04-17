package com.example.home.ui.dataview

import com.example.data.models.CastView
import com.example.data.models.MovieDetailResponse

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
data class MovieDetailView(
    val detail: MovieDetailResponse,
    val cast: List<CastView>,
    val recommendation: List<MovieView>
)