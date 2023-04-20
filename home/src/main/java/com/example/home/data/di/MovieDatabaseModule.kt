package com.example.home.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.*
import com.example.home.data.local.MovieDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Module
@InstallIn(SingletonComponent::class)
object MovieDatabaseModule {

    private const val DB_NAME = "com.romellfudi.imdbkata.db"

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDataBase {
        return Room.databaseBuilder(context, MovieDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(movieDataBase: MovieDataBase) = movieDataBase.movieDao()

    @Provides
    fun provideGenreDao(movieDataBase: MovieDataBase) = movieDataBase.genreDao()

    @Provides
    fun provideUserFavDao(movieDataBase: MovieDataBase) = movieDataBase.userFavDao()

}