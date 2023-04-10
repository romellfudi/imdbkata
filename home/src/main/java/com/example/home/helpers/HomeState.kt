package com.example.home.helpers

sealed class HomeState {

    object Loading : HomeState()

    object Ready : HomeState()

    data class Error(val message: String) : HomeState()
}
