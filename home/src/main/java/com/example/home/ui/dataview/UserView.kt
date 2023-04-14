package com.example.home.ui.dataview

import com.example.data.models.User


/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
data class UserView(
    val name: String,
    val email: String,
    val photoUrl: String
)

fun User.toUserView(): UserView {
    return UserView(
        name = name,
        email = email,
        photoUrl = photoUrl
    )
}

// add extend function to check photoUrl is contains null as string
fun UserView.picAvailable(): Boolean {
    return photoUrl != "null" && photoUrl.isNotEmpty()
}