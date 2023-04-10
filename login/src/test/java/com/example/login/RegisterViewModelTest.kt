package com.example.login

import app.cash.turbine.test
import com.example.core.tool.sha256
import com.example.login.helpers.LoginState
import com.example.login.helpers.LoginViewModelState
import com.example.login.ui.viewmodels.RegisterViewModel
import com.example.login.usecase.SignupUseCase
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
class RegisterViewModelTest {

    private val dispatcherProvider = ViewModelDispatcherProvider()

    @MockK
    lateinit var signupUseCase: SignupUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test no register an invalid data`() = runBlocking {
        // Create a mock IsLoggedUseCase object that returns LoggedState.NotLogged
        every { signupUseCase.invoke("", "", "12345678".sha256()) }.returns(
            flowOf(LoginState.Error("Error"))
        )
        // Create a RegisterViewModel object with the mock use case and dispatcher
        val viewModel = RegisterViewModel(signupUseCase, dispatcherProvider)
        viewModel.shouldRegister.test {
            // Call the checkUserLogged method to trigger the flow emission
            viewModel.shouldRegister("", "", "12345678")
            // Assert that the correct value was emitted by the flow
            assertEquals("Error", (awaitItem() as LoginViewModelState.Error).message)
            // Cancel the flow collector to prevent it from waiting for more events
            cancelAndIgnoreRemainingEvents()
        }

        // Verify that the mock use case was invoked
        verify { signupUseCase.invoke("", "", "12345678".sha256()) }
    }

    @Test
    fun `test register an valid data`() = runBlocking {
        // Create a mock SignupUseCase object that returns true
        every {
            signupUseCase.invoke(
                "romellfudi",
                "romell.dominguez@gmail.net",
                "123qweASD!".sha256()
            )
        }.returns(flowOf(LoginState.Success("@gmail.net")))
        // Create a RegisterViewModel object with the mock use case and dispatcher
        val viewModel = RegisterViewModel(signupUseCase, dispatcherProvider)
        viewModel.shouldRegister.test {
            // Call the shouldRegister method to trigger the flow emission
            viewModel.shouldRegister("romellfudi", "romell.dominguez@gmail.net", "123qweASD!")
            // Assert that the correct value was emitted by the flow
            assertEquals("@gmail.net",
                (awaitItem() as LoginViewModelState.Success).userEmailOrName)
            // Cancel the flow collector to prevent it from waiting for more events
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the mock use case was invoked
        verify {
            signupUseCase.invoke(
                "romellfudi",
                "romell.dominguez@gmail.net",
                "123qweASD!".sha256()
            )
        }

    }

}
