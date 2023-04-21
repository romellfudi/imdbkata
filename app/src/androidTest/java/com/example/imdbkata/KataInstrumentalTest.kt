package com.example.imdbkata

import android.Manifest
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.imdbkata.ui.MainActivity
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.kakao.common.utilities.getResourceString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
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

@RunWith(AndroidJUnit4::class)
class KataInstrumentalTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

    private val username = "Freddy"
    private val randomString = (1..5).map { ('a'..'z').random() }.joinToString("")
    private val badEmail = "${username.toLowerCase()}.$randomString@example"
    private val goodEmail = "$badEmail.com"
    private val goodPassword = "123qweASD!"
    private val badPassword1 = "123qweASD"
    private val badPassword2 = "123qwe!@"
    private val badPassword3 = "12345678910"

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.INTERNET
    )

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun goodWorkflow() = run {
        step("Open Splash screen") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                imdbLogoImage { assertIsDisplayed() }
            }
        }
        step("Register new user") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                newRegisterButton {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.to_register))
                    performClick()
                }
                nameRFieldText {
                    assertIsDisplayed()
                    performTextInput(username)
                }
                emailRFieldText {
                    assertIsDisplayed()
                    performTextInput(goodEmail)
                }
                passwordRFieldText {
                    assertIsDisplayed()
                    performTextInput(goodPassword)
                }
                performCloseSoftKeyBoard()
                registerButton {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.accept))
                    performClick()
                }
            }
        }
        step("Open Home screen") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                homeLazyColumn {
                    assertIsDisplayed()
                    hasScrollAction()
                    performScrollToIndex(7)
                    performScrollToIndex(5)
                    performScrollToIndex(11)
                    performScrollToIndex(3)
                }
                searchFieldText {
                    assertIsDisplayed()
                    performTextInput("the")
                    performTextClearance()
                }
                homeLazyColumn {
                    performScrollToIndex(7)
                    performScrollToIndex(2)
                    performScrollToIndex(8)
                    performScrollToIndex(1)
                }
            }
        }
        step("Sign out and go back to Login") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                performBackAction()
            }
        }

    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun goodWorkflowAsGuest() = run {
        step("Open Splash screen") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                imdbLogoImage { assertIsDisplayed() }
            }
        }
        step("Go to Home screen as guest user") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                guestAction {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.guest_text))
                    performClick()
                }
            }
        }
        step("Open Home screen") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                homeLazyColumn {
                    assertIsDisplayed()
                    hasScrollAction()
                    performScrollToIndex(7)
                    performScrollToIndex(5)
                    performScrollToIndex(11)
                    performScrollToIndex(3)
                }
                searchFieldText {
                    assertIsDisplayed()
                    performTextInput("the")
                    performTextClearance()
                }
                homeLazyColumn {
                    performScrollToIndex(7)
                    performScrollToIndex(2)
                    performScrollToIndex(8)
                    performScrollToIndex(1)
                }
                performBackAction()
            }
        }
    }

    @Test
    fun wrongEmailWorkflow() = run {
        step("Trying to register new user with wrong email format") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                newRegisterButton {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.to_register))
                    performClick()
                }
                nameRFieldText {
                    assertIsDisplayed()
                    performTextInput(username)
                }
                emailRFieldText {
                    assertIsDisplayed()
                    performTextInput(badEmail)
                }
                passwordRFieldText {
                    assertIsDisplayed()
                    performTextInput(goodPassword)
                }
                performCloseSoftKeyBoard()
                registerButton {
                    assertIsDisplayed()
                    assertIsNotEnabled()
                }
            }
        }
    }

    @Test
    fun wrongPasswordWorkflow() = run {
        step("Trying to register new user with wrong password format") {
            onComposeScreen<ComposeMainActivity>(composeTestRule) {
                newRegisterButton {
                    assertIsDisplayed()
                    assertTextEquals(getResourceString(R.string.to_register))
                    performClick()
                }
                nameRFieldText {
                    assertIsDisplayed()
                    performTextInput(username)
                }
                emailRFieldText {
                    assertIsDisplayed()
                    performTextInput(goodEmail)
                }
                passwordRFieldText {
                    assertIsDisplayed()
                    performTextInput(badPassword1)
                }
                performCloseSoftKeyBoard()
                registerButton {
                    assertIsDisplayed()
                    assertIsNotEnabled()
                }
                passwordRFieldText { performTextReplacement(badPassword2) }
                performCloseSoftKeyBoard()
                registerButton { assertIsNotEnabled() }
                passwordRFieldText { performTextReplacement(badPassword3) }
                performCloseSoftKeyBoard()
                registerButton { assertIsNotEnabled() }
            }
        }
    }

    private fun performBackAction() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun performCloseSoftKeyBoard() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }
}
