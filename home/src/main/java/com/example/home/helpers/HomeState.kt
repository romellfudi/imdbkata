package com.example.home.helpers

sealed class HomeState {

    // Loading state
    object Loading : HomeState()

    // Ready state with a list of objects
    object Ready : HomeState()

    // Error state with a message
    data class Error(val message: String) : HomeState()
}
