package com.example.login.ui.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.core.tool.sha256
import com.example.login.helpers.LoginState
import com.example.login.helpers.LoginViewModelState
import com.example.login.usecase.SignInAnonymouslyUseCase
import com.example.login.usecase.SignInEmailAndPasswordUseCase
import com.example.login.usecase.SignInFacebookUseCase
import com.example.login.usecase.SignInGoogleUseCase
import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * LoginViewModel class to handle the login process
 *
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val signInEmailAndPasswordUseCase: SignInEmailAndPasswordUseCase,
    private val signInGoogleUseCase: SignInGoogleUseCase,
    private val signInFacebookUseCase: SignInFacebookUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _shouldNavigateRegister = mutableNotReplayFlow<Boolean>()
    val shouldNavigateRegister: SharedFlow<Boolean> = _shouldNavigateRegister
    private val _shouldNavigateHome = mutableNotReplayFlow<LoginViewModelState>()
    val shouldNavigateHome: SharedFlow<LoginViewModelState> = _shouldNavigateHome

    // StateFlow to handle the email and password fields and the login button
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val isReadyToLogin: Flow<Boolean> = combine(
        email.asStateFlow(), password.asStateFlow()
    ) { email, password ->
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.isNotBlank() && password.length >= 8
    }.distinctUntilChanged()

    // function to force the email field to be lowercase and trim
    fun setUsernameText(text: String) {
        email.value = text.trim().lowercase()
    }

    // function to force the password field to be trim
    fun setUPasswordText(text: String) {
        password.value = text.trim()
    }

    fun goToRegisterView() {
        _shouldNavigateRegister.tryEmit(true)
    }

    fun signAnonymous() {
        viewModelScope.launch(dispatcherProvider.main) {
            signInAnonymouslyUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message.orEmpty()))
                }
                .collect {
                    when (it) {
                        is LoginState.Success ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Success())
                        is LoginState.Error ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message))
                    }
                }
        }
    }

    fun loginWithEmailPassword() {
        viewModelScope.launch(dispatcherProvider.main) {
            signInEmailAndPasswordUseCase(email.value, password.value.sha256())
                .flowOn(dispatcherProvider.main)
                .catch {
                    _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message.orEmpty()))
                }
                .collect {
                    when (it) {
                        is LoginState.Success ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Success(it.userEmailOrName))
                        is LoginState.Error ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message))
                    }
                }
        }
    }


    fun signFacebook(token: AccessToken) {
        viewModelScope.launch(dispatcherProvider.main) {
            signInFacebookUseCase(token)
                .flowOn(dispatcherProvider.main)
                .catch {
                    _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message.orEmpty()))
                }
                .collect {
                    when (it) {
                        is LoginState.Success ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Success(it.userEmailOrName))
                        is LoginState.Error ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message))
                    }
                }
        }
    }

    fun signGoogle(credential: AuthCredential) {
        viewModelScope.launch(dispatcherProvider.main) {
            signInGoogleUseCase(credential)
                .flowOn(dispatcherProvider.main)
                .catch {
                    _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message.orEmpty()))
                }.collect {
                    when (it) {
                        is LoginState.Success ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Success(it.userEmailOrName))
                        is LoginState.Error ->
                            _shouldNavigateHome.tryEmit(LoginViewModelState.Error(it.message))
                    }
                }
        }
    }

}