package com.example.home.ui.dataview

import com.example.data.models.Movie
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
