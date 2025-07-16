package com.example.githubusers.ui.userDetail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.githubusers.ui.Utils.REPO
import org.junit.Rule
import org.junit.Test

class RepoInfoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun repoInfoCard_displaysAllRepoInfo_and_isClickable() {
        var clickedUrl: String? = null

        composeTestRule.setContent {
            RepoInfoCard(repo = REPO, openLink = { clickedUrl = it })
        }

        composeTestRule.onNodeWithText(REPO.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(" ${REPO.stargazersCount}").assertIsDisplayed()
        composeTestRule.onNodeWithText(REPO.language ?: "N/A").assertIsDisplayed()
        composeTestRule.onNodeWithText(REPO.description ?: "No Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("url: ${REPO.htmlUrl}").assertIsDisplayed()

        composeTestRule.onNodeWithText(REPO.name).performClick()
        assert(clickedUrl == REPO.htmlUrl)
    }
}