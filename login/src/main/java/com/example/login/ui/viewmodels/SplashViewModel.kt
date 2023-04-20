package com.example.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.login.helpers.LoggedState
import com.example.login.usecase.IsLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SplashViewModel class for the Application
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isLoggedUseCase: IsLoggedUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    companion object {
        const val SPLASH_DELAY = 1500L
    }

    private val _isLogged = mutableNotReplayFlow<Boolean>()
    val isLogged: SharedFlow<Boolean> = _isLogged

    init {
        checkUserLogged()
    }

    fun checkUserLogged() {
        viewModelScope.launch(dispatcherProvider.main) {
            isLoggedUseCase()
                .debounce(SPLASH_DELAY)
                .onEach { state -> _isLogged.emit(state is LoggedState.Logged) }
                .catch { _isLogged.emit(false) }
                .launchIn(viewModelScope)
        }
    }
}