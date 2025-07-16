package com.example.githubusers.ui

import com.example.githubusers.data.modal.Repo
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.modal.UserDetail

/**
 * Utility object containing constants and sample data for testing purposes and compose preview.
 */
object Utils {
    const val AVATAR = "Avatar"
    const val ERROR_MESSAGE = "An error occurred while fetching data. Please try again later."
    private const val GITHUB_ID = 1
    private const val GITHUB_LOGIN = "sampleuser"
    private const val GITHUB_NAME = "Sample User"
    private const val GITHUB_FOLLOWERS = 100
    private const val GITHUB_FOLLOWING = 50
    val USER = User(
        id = GITHUB_ID,
        login = GITHUB_LOGIN,
        avatarUrl = "",
    )
    val USER_DETAIL = UserDetail(
        id = GITHUB_ID,
        login = GITHUB_LOGIN,
        name = GITHUB_NAME,
        avatarUrl = "",
        followers = GITHUB_FOLLOWERS,
        following = GITHUB_FOLLOWING,
        url = "",
        htmlUrl = "",
    )
    val REPO = Repo(
        id = 1,
        name = "Sample Repository",
        description = "This is a sample repository description.",
        stargazersCount = 150,
        language =  "Kotlin",
        htmlUrl = "",
        fork = false
    )
}