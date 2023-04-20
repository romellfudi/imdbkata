package com.example.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.core.tool.sha256
import com.example.login.helpers.LoginState
import com.example.login.helpers.LoginViewModelState
import com.example.login.helpers.Patterns.Companion.emailPattern
import com.example.login.helpers.Patterns.Companion.passwordPattern
import com.example.login.helpers.Patterns.Companion.userNamePattern
import com.example.login.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Register
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _shouldBack = mutableNotReplayFlow<Boolean>()
    val shouldBack: SharedFlow<Boolean> = _shouldBack
    private val _shouldRegister = mutableNotReplayFlow<LoginViewModelState>()
    val shouldRegister: SharedFlow<LoginViewModelState> = _shouldRegister
    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val isEmailValid: Flow<Boolean> = email.map {
        it.isNotBlank() && emailPattern.matches(it)
    }.distinctUntilChanged()
    val isPasswordValid: Flow<Boolean> = password.map {
        it.isNotBlank() && passwordPattern.matches(it)
    }.distinctUntilChanged()
    val isReadyToRegister: Flow<Boolean> = combine(name, isEmailValid, isPasswordValid)
    { name, emailValid, passwordValid ->
        name.isNotBlank() && emailValid && passwordValid
    }.distinctUntilChanged()

    fun onBack() {
        _shouldBack.tryEmit(true)
    }

    fun setName(newName: String) {
        if (newName.isEmpty() || userNamePattern.matches(newName)) {
            name.value = newName
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch(dispatcherProvider.main) {
            signupUseCase(name, email, password.sha256())
                .flowOn(dispatcherProvider.main)
                .onEach {
                    when (it) {
                        is LoginState.Success ->
                            _shouldRegister.emit(LoginViewModelState.Success(it.userEmailOrName))
                        is LoginState.Error ->
                            _shouldRegister.emit(LoginViewModelState.Error(it.message))
                    }
                }
                .catch { _shouldRegister.emit(LoginViewModelState.Error(it.message.orEmpty())) }
                .launchIn(viewModelScope)
        }

    }

    fun cleanView() {
        name.value = ""
        email.value = ""
        password.value = ""
    }
}