package com.example.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@JsonClass(generateAdapter = true)
data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)