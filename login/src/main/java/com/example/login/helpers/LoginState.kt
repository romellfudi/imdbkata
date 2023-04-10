package com.example.login.helpers

sealed class LoginState {

    /**
     * Represents the state of the login operation when it has successfully completed.
     */
    data class Success(val userEmailOrName: String = "") : LoginState()

    /**
     * Represents the state of the login operation when it has failed, and contains an error message.
     */
    data class Error(val message: String) : LoginState()
}