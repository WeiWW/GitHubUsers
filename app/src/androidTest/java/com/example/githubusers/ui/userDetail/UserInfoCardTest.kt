package com.example.githubusers.ui.userDetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.githubusers.ui.Utils.AVATAR
import com.example.githubusers.ui.Utils.USER_DETAIL
import org.junit.Rule
import org.junit.Test

class UserInfoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun userInfoCard_displaysUserInfo() {
        val userDetail = USER_DETAIL

        composeTestRule.setContent {
            UserInfoCard(userDetail = userDetail)
        }

        composeTestRule.onNodeWithContentDescription(AVATAR).assertIsDisplayed()
        composeTestRule.onNodeWithText(userDetail.name.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText("@${userDetail.login}").assertIsDisplayed()
        composeTestRule.onNodeWithText("Followers: ${userDetail.followers}").assertIsDisplayed()
        composeTestRule.onNodeWithText("Following: ${userDetail.following}").assertIsDisplayed()
    }
}