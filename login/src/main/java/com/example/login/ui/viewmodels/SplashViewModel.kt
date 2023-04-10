package com.example.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.login.helpers.LoggedState
import com.example.login.usecase.IsLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
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

    private val _timeFinished = mutableNotReplayFlow<Boolean>()
    val timeFinished: SharedFlow<Boolean> = _timeFinished

    init {
        initEvent()
    }

    fun initEvent() {
        viewModelScope.launch(dispatcherProvider.main) {
            delay(SPLASH_DELAY)
            checkUserLogged()
        }
    }

    fun checkUserLogged() {
        viewModelScope.launch(dispatcherProvider.main) {
            isLoggedUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _timeFinished.emit(false)
                }
                .collect {  state ->
                    _timeFinished.emit(state is LoggedState.Logged)
                }
        }
    }
}