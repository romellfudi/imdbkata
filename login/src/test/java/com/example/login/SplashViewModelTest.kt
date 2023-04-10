package com.example.login

import app.cash.turbine.test
import com.example.login.helpers.LoggedState
import com.example.login.ui.viewmodels.SplashViewModel
import com.example.login.usecase.IsLoggedUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class SplashViewModelTest {

    private val dispatcherProvider = ViewModelDispatcherProvider()
    private lateinit var viewModel: SplashViewModel

    @MockK
    lateinit var isLoggedUseCase: IsLoggedUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test user is not logged`() = runBlocking {
        // Create a mock IsLoggedUseCase object that returns LoggedState.NotLogged
        every { isLoggedUseCase.invoke() }.returns(
            flowOf(LoggedState.NotLogged)
        )
        // Create a SplashViewModel object with the mock use case and dispatcher
        viewModel = SplashViewModel(isLoggedUseCase, dispatcherProvider)
        // Use a flow collector to capture the values emitted by timeFinished
        viewModel.timeFinished.test {
            // Call the checkUserLogged method to trigger the flow emission
            viewModel.checkUserLogged()
            // Assert that the correct value was emitted by the flow
            assertEquals(false, awaitItem())
            // Cancel the flow collector to prevent it from waiting for more events
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the mock use case was invoked
        verify { isLoggedUseCase.invoke() }
    }

    @Test
    fun `test user is logged`() = runBlocking {
        // Create a mock IsLoggedUseCase object that returns LoggedState.Logged
        every { isLoggedUseCase.invoke() }.returns(
            flowOf(LoggedState.Logged)
        )
        // Create a SplashViewModel object with the mock use case and dispatcher
        viewModel = SplashViewModel(isLoggedUseCase, dispatcherProvider)
        // Use a flow collector to capture the values emitted by timeFinished
        viewModel.timeFinished.test {
            // Call the checkUserLogged method to trigger the flow emission
            viewModel.checkUserLogged()
            // Assert that the correct value was emitted by the flow
            assertEquals(true, awaitItem())
            // Cancel the flow collector to prevent it from waiting for more events
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the mock use case was invoked
        verify { isLoggedUseCase.invoke() }
    }

}
