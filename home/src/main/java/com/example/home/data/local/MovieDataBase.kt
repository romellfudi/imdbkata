package com.example.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.Converters
import com.example.data.models.Genre
import com.example.data.models.Movie
import com.example.data.models.UserFav

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Database(entities = [Movie::class, Genre::class, UserFav::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao

    abstract fun genreDao(): GenresDao

    abstract fun userFavDao(): UserFavDao
}