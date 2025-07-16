package com.example.githubusers.ui.componets

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.Modifier
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.githubusers.ui.Utils.ERROR_MESSAGE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorBoxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorBox_displaysErrorMessage() {
        composeTestRule.setContent {
            ErrorBox(ERROR_MESSAGE, Modifier)
        }
        composeTestRule.onNodeWithText(ERROR_MESSAGE).assertIsDisplayed()
    }
}