package com.example.login.helpers

sealed class LoginViewModelState {

    /**
     * Represents the state of the login operation when it has successfully completed.
     * Empty string refers to guest user.
     * @param userEmailOrName the email or name of the user that has logged in.
     */
    data class Success(val userEmailOrName: String = "") : LoginViewModelState()

    /**
     * Represents the state of the login operation when it has failed, and contains an error message.
     * @param message the error message.
     */
    data class Error(val message: String) : LoginViewModelState()
}