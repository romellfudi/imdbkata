package com.example.data.models

import com.squareup.moshi.Json

data class CreditsResponse(
    val id: Int,
    val cast: List<CastResponse>
)

data class CastResponse(
    val name: String,
    val id: Int,
    @Json(name = "original_name")
    val originalName: String,
    @Json(name = "profile_path")
    val profilePath: String?
) {
    fun toCastModel(): CastModel {
        return CastModel(
            id = id,
            name = name,
            profilePath = profilePath,
            originalName = originalName
        )
    }
}
data class CastModel(
    val id: Int,
    val name: String,
    val originalName: String,
    val profilePath:String?
)
