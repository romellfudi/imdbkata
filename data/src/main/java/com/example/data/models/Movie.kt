package com.example.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Entity(
    tableName = "movies"
)
@JsonClass(generateAdapter = true)
data class Movie(
    val adult: Boolean,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: Int,
    @Json(name = "original_language")
    val originalLanguage: String,
    @Json(name = "original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int,
    var typeRank: String = "",
    @ColumnInfo(name = "recommended_movies")
    val recommendedMovies: List<Int> = listOf(0)
) : Serializable {

    val posterUrl: String
        get() = if (posterPath.isNullOrEmpty())
            "https://upload.wikimedia.org/wikipedia/commons/6/64/Poster_not_available.jpg"
        else
            "https://image.tmdb.org/t/p/w500$posterPath"
}