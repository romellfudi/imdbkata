package com.example.login.helpers

sealed class LoggedState {

    object Logged : LoggedState()

    object NotLogged : LoggedState()
}