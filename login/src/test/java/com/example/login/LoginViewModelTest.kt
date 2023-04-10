package com.example.login

import app.cash.turbine.test
import com.example.core.tool.sha256
import com.example.login.helpers.LoginState
import com.example.login.helpers.LoginViewModelState
import com.example.login.ui.viewmodels.LoginViewModel
import com.example.login.usecase.*
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase
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
class LoginViewModelTest {

    private val dispatcherProvider = ViewModelDispatcherProvider()

    @MockK
    lateinit var signInAnonymouslyUseCase: SignInAnonymouslyUseCase

    @MockK
    lateinit var signInEmailAndPasswordUseCase: SignInEmailAndPasswordUseCase

    @MockK
    lateinit var signInGoogleUseCase: SignInGoogleUseCase

    @MockK
    lateinit var signInFacebookUseCase: SignInFacebookUseCase

    @MockK
    lateinit var facebookToken: AccessToken

    @MockK
    lateinit var googleToken: AuthCredential

    private val userName = "username"
    private val userEmail = "romell.dominguez@gmail.net"

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `test Sign in as Anonymous`() = runBlocking {
        // Create a mock SignInAnonymouslyUseCase object that returns a Success state
        every { signInAnonymouslyUseCase.invoke() }.returns(
            flowOf(LoginState.Success())
        )
        val viewModel = LoginViewModel(
            signInAnonymouslyUseCase, signInEmailAndPasswordUseCase,
            signInGoogleUseCase, signInFacebookUseCase, dispatcherProvider
        )
        viewModel.shouldNavigateHome.test {
            viewModel.signAnonymous()
            TestCase.assertEquals(LoginViewModelState.Success(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the use case was called
        verify { signInAnonymouslyUseCase.invoke() }
    }

    @Test
    fun `test Sign in as Email and Password`() = runBlocking {
        // Create a mock SignInEmailAndPasswordUseCase object that returns a Success state
        every { signInEmailAndPasswordUseCase.invoke("", "".sha256()) }.returns(
            flowOf(LoginState.Success(userEmail))
        )
        val viewModel = LoginViewModel(
            signInAnonymouslyUseCase, signInEmailAndPasswordUseCase,
            signInGoogleUseCase, signInFacebookUseCase, dispatcherProvider
        )
        viewModel.shouldNavigateHome.test {
            viewModel.loginWithEmailPassword()
            TestCase.assertEquals(LoginViewModelState.Success(userEmail), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the use case was called
        verify { signInEmailAndPasswordUseCase.invoke("", "".sha256()) }
    }

    @Test
    fun `test Sign in as Facebook`() = runBlocking {
        // Create a mock SignInFacebookUseCase object that returns a Success state
        every { signInFacebookUseCase.invoke(facebookToken) }.returns(
            flowOf(LoginState.Success(userName))
        )
        val viewModel = LoginViewModel(
            signInAnonymouslyUseCase, signInEmailAndPasswordUseCase,
            signInGoogleUseCase, signInFacebookUseCase, dispatcherProvider
        )
        viewModel.shouldNavigateHome.test {
            viewModel.signFacebook(facebookToken)
            TestCase.assertEquals(LoginViewModelState.Success(userName), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the use case was called
        verify { signInFacebookUseCase.invoke(facebookToken) }
    }

    @Test
    fun `test Sign in as Google user`() = runBlocking {
        // Create a mock SignInGoogleUseCase object that returns a Success state
        every { signInGoogleUseCase.invoke(googleToken) }.returns(
            flowOf(LoginState.Success(userName))
        )
        val viewModel = LoginViewModel(
            signInAnonymouslyUseCase, signInEmailAndPasswordUseCase,
            signInGoogleUseCase, signInFacebookUseCase, dispatcherProvider
        )
        viewModel.shouldNavigateHome.test {
            viewModel.signGoogle(googleToken)
            TestCase.assertEquals(LoginViewModelState.Success(userName), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        // Verify that the use case was called
        verify { signInGoogleUseCase.invoke(googleToken) }
    }

}
