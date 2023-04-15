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
data class CastModel(
    val id: Int,
    val name: String,
    val originalName: String,
    val profilePath:String?
) {

    val profileUrl: String
        get() = if (profilePath.isNullOrEmpty())
            "https://upload.wikimedia.org/wikipedia/commons/6/64/Poster_not_available.jpg"
        else
            "https://image.tmdb.org/t/p/w500$profilePath"
}

fun CastResponse.toCastModel(): CastModel {
    return CastModel(
        id = id,
        name = name,
        originalName = originalName,
        profilePath = profilePath
    )
}