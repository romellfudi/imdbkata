package com.example.core.di

import android.content.Context
import android.content.SharedPreferences
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
object SharePreferenceModule {

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("romellfudi.imdbkata", Context.MODE_PRIVATE)
    }

}