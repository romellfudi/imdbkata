package com.example.data.models

import com.squareup.moshi.JsonClass

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@JsonClass(generateAdapter = true)
data class GenresResponse(
    val genres: List<Genre>
)