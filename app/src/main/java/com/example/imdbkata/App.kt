package com.example.imdbkata

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}