package com.example.imdbkata

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ComposeMainActivity(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ComposeMainActivity>(
        semanticsProvider = semanticsProvider,
    ) {
    val imdbLogoImage: KNode = child { hasTestTag("splash_screen_image") }
    val emailFieldText: KNode = child { hasTestTag("login_screen_email") }
    val passwordFieldText: KNode = child { hasTestTag("login_screen_password") }
    val loginButton: KNode = child { hasTestTag("login_screen_login_button") }
    val newRegisterButton: KNode = child { hasTestTag("login_screen_new_register_button") }
    val guestAction: KNode = child { hasTestTag("login_screen_guest_action") }
    val searchFieldText: KNode = child { hasTestTag("home_screen_search_field") }
    val homeLazyColumn: KNode = child { hasTestTag("home_screen_list") }
    val nameRFieldText: KNode = child { hasTestTag("register_screen_name") }
    val emailRFieldText: KNode = child { hasTestTag("register_screen_email") }
    val passwordRFieldText: KNode = child { hasTestTag("register_screen_password") }
    val registerButton: KNode = child { hasTestTag("register_screen_register_button") }
}