package com.example.imdbkata

import android.Manifest
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.imdbkata.ui.MainActivity
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.kakao.common.utilities.getResourceString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@RunWith(AndroidJUnit4::class)
class DojoInstrumentalTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {

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
                    performTextInput(userName)
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
                    performTextInput(userName)
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
                    performTextInput(userName)
                }
                emailRFieldText {
                    assertIsDisplayed()
                    performTextInput(goodEmail)
                }
                passwordRFieldText {
                    assertIsDisplayed()
                    performTextInput(badPasswordNotEspecialCharacters)
                }
                performCloseSoftKeyBoard()
                registerButton {
                    assertIsDisplayed()
                    assertIsNotEnabled()
                }
                passwordRFieldText { performTextReplacement(badPasswordNotCapitalizeLetters) }
                performCloseSoftKeyBoard()
                registerButton { assertIsNotEnabled() }
                passwordRFieldText { performTextReplacement(badPasswordOnlyNumbers) }
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
