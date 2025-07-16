package com.example.githubusers.ui.userList

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.githubusers.ui.Utils.AVATAR
import com.example.githubusers.ui.Utils.USER
import org.junit.Rule
import org.junit.Test

class UserCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun userCard_displaysUsername_and_isClickable() {
        val user = USER

        var clicked = false

        composeTestRule.setContent {
            UserCard(user = user, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("@${user.login}").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(AVATAR).assertIsDisplayed()
        composeTestRule.onNodeWithText("@${user.login}").performClick()
        assert(clicked)
    }
}