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
    tableName = "genres"
)
@JsonClass(generateAdapter = true)
data class Genre(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: Int,
    val name: String,
) : Serializable