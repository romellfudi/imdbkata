package com.example.data.models

data class User(
    val name: String,
    val email: String,
    val photoUrl: String,
    val provider: String,
    val isAnonymous: Boolean = false
    )