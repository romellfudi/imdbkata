package com.example.home.ui.dataview

import com.example.data.models.Movie


/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
data class MovieDetailView(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val posterUrl: String
)