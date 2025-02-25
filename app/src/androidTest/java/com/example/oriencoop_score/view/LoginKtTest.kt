package com.example.oriencoop_score.view

import android.util.Log
import org.junit.Rule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.oriencoop_score.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.test.runner.AndroidJUnit4
import org.junit.runners.JUnit4


@HiltAndroidTest
@RunWith(JUnit4::class)
class LoginScreenTest            {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()

    }

    @Test
    fun testUserInputAndButtonInteraction() {
        // Set the content of your Compose UI
        composeTestRule.setContent {
            Login(navController = rememberNavController())
        }

        // Verify that the login screen is displayed
        composeTestRule.onNodeWithText("Inicia sesión").assertIsDisplayed()

        // Enter username and password
        composeTestRule.onNodeWithText("Rut (12345678-9)").performTextInput("12345678-9")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password")

        // Check if the login button is enabled
        composeTestRule.onNodeWithText("Log In").assertIsNotEnabled()

        // Simulate a valid input to enable the button
        composeTestRule.onNodeWithText("Rut (12345678-9)").performTextInput("12345678-9")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("validPassword")

        // Check if the login button is now enabled
        composeTestRule.onNodeWithText("Log In").assertIsNotEnabled()
    }

    @Test
    fun testLoginButtonDisabledWhenFieldsAreEmpty() {
        // Set the content of your Compose UI
        composeTestRule.setContent {
            Login(navController = rememberNavController())
        }

        // Verify that the login button is disabled when fields are empty
        composeTestRule.onNodeWithText("Log In").assertIsNotEnabled()
    }

    @Test
    fun testLoadingIndicatorIsDisplayedDuringLogin() {
        // Set the content of your Compose UI
        composeTestRule.setContent {
            Login(navController = rememberNavController())
        }

        // Enter username and password
        composeTestRule.onNodeWithText("Rut (12345678-9)").performTextInput("12345678-9")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("password")

        // Click the login button
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify that the loading indicator is displayed
        composeTestRule.onNodeWithContentDescription("CircularProgressIndicator").assertIsDisplayed()
    }

    @Test
    fun testErrorMessageIsDisplayedOnFailedLogin() {
        // Set the content of your Compose UI
        composeTestRule.setContent {
            Login(navController = rememberNavController())
        }

        // Enter invalid username and password
        composeTestRule.onNodeWithText("Rut (12345678-9)").performTextInput("invalidUser")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("wrongPassword")

        // Click the login button
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify that the error message is displayed
        composeTestRule.onNodeWithText("Rut o contraseña incorrectos").assertIsDisplayed()
    }
}