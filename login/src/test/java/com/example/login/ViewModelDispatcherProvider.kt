package com.example.login

import com.example.core.di.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * A [DispatcherProvider] that uses [UnconfinedTestDispatcher] for all dispatchers.
 */
@ExperimentalCoroutinesApi
class ViewModelDispatcherProvider : DispatcherProvider {

    private val testDispatcher = UnconfinedTestDispatcher()

    /**
     * The [DispatcherProvider.main] is used for all dispatchers.
     */
    override val main: CoroutineDispatcher
        get() = testDispatcher

    /**
     * The [DispatcherProvider.io] is used for all dispatchers.
     */
    override val io: CoroutineDispatcher
        get() = testDispatcher

    /**
     * The [DispatcherProvider.default] is used for all dispatchers.
     */
    override val default: CoroutineDispatcher
        get() = testDispatcher
}
