package com.example.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Entity(
    tableName = "user_fav"
)
@JsonClass(generateAdapter = true)
data class UserFav(
    @Json(name = "user_mail")
    val userMail: String,
    @Json(name = "movie_id")
    val movieId: Int,
    @Json(name = "poster_path")
    val posterPath: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0
) : Serializable