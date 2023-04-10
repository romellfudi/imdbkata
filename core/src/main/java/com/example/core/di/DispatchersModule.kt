package com.example.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}

interface DispatcherProvider {

    val main: CoroutineDispatcher

    val io: CoroutineDispatcher

    val default: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
}
