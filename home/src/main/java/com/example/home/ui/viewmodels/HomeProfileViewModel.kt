package com.example.home.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.DispatcherProvider
import com.example.core.tool.mutableNotReplayFlow
import com.example.home.helpers.HomeState
import com.example.home.ui.dataview.UserView
import com.example.home.ui.dataview.toUserView
import com.example.home.usecase.firebase.GetFirebaseUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@HiltViewModel
class HomeProfileViewModel @Inject constructor(
    private val getFirebaseUserUseCase: GetFirebaseUserUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _user = mutableNotReplayFlow<UserView?>()
    val user: SharedFlow<UserView?> = _user
    private val _isLoading = mutableStateOf<HomeState>(HomeState.Loading)
    val isLoading: State<HomeState> = _isLoading

    companion object {
        const val DEBOUNCE_PERIOD = 500L
    }

    fun fetchUser() {
        viewModelScope.launch(dispatcherProvider.main) {
            getFirebaseUserUseCase()
                .flowOn(dispatcherProvider.main)
                .catch {
                    _user.emit(null)
                }
                .collect { state ->
                    _user.emit(state?.toUserView())
                }
        }
    }

}