package com.example.oriencoop_score.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsEnabled
import org.junit.Rule

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.oriencoop_score.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.bytebuddy.build.Plugin.Engine.Dispatcher.Materializable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
class LoginScreenTest {

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
            MaterialTheme {
                LoginScreen(navController = rememberNavController())
            }
        }

        // Enter username and password
        composeTestRule.onNodeWithTag("Rut (12345678-9)").performTextInput("12345678-9")
        composeTestRule.onNodeWithTag("Contraseña").performTextInput("password")

        // Check if the login button is enabled
        composeTestRule.onNodeWithTag("Log In").assertIsEnabled()
    }
}