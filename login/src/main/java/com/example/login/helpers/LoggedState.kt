package com.example.login.helpers

/**
 * This class is used to represent the state of the user's login.
 * It can be either logged or not logged.
 */
sealed class LoggedState {

    /**
     * This object represents the state of the user being logged.
     */
    object Logged : LoggedState()

    /**
     * This object represents the state of the user being not logged.
     */
    object NotLogged : LoggedState()
}