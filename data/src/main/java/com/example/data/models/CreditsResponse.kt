package com.example.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditsResponse(
    val id: Int,
    val cast: List<CastResponse>
)

@JsonClass(generateAdapter = true)
data class CastResponse(
    val name: String,
    val id: Int,
    @Json(name = "original_name")
    val originalName: String,
    @Json(name = "profile_path")
    val profilePath: String?,
    val character: String
)

data class CastView(
    val id: Int,
    val name: String,
    val originalName: String,
    val profilePath: String?
) {

    val profileUrl: String
        get() = if (profilePath.isNullOrEmpty())
            "https://upload.wikimedia.org/wikipedia/commons/6/64/Poster_not_available.jpg"
        else
            "https://image.tmdb.org/t/p/w500$profilePath"
}

fun CastResponse.toCastModel(): CastView {
    return CastView(
        id = id,
        name = name,
        originalName = originalName,
        profilePath = profilePath
    )
}

val castViewList = listOf(
    CastView(
        id = 3131,
        name = "Antonio Banderas",
        originalName = "Antonio Banderas",
        profilePath = "/n8YlGookYzgD3cmpMP45BYRNIoh.jpg"
    ),
    CastView(
        id = 109,
        name = "Elijah Wood",
        originalName = "Elijah Wood",
        profilePath = "/7UKRbJBNG7mxBl2QQc5XsAh6F8B.jpg"
    ),
    CastView(
        id = 1327,
        name = "Ian McKellen",
        originalName = "Ian McKellen",
        profilePath = "/5cnnnpnJG6TiYUSS7qgJheUZgnv.jpg"
    ),
    CastView(
        id = 6384,
        name = "Keanu Reeves",
        originalName = "Keanu Reeves",
        profilePath = "/4D0PpNI0kmP58hgrwGC3wCjxhnm.jpg"
    ),
)