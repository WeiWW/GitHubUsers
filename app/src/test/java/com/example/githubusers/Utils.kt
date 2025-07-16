package com.example.githubusers

import androidx.recyclerview.widget.ListUpdateCallback
import com.example.githubusers.data.modal.Repo
import com.example.githubusers.data.modal.SearchResult
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.remote.GitHubApiService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object MockServerUtil {
    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: GitHubApiService

    fun setUp() {
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    fun tearDown() {
        mockWebServer.shutdown()
    }

    fun enqueueMockResponse(responseCode: Int, responseBody: String = "") {
        val mockResponse = MockResponse().setResponseCode(responseCode).setBody(responseBody)
        mockWebServer.enqueue(mockResponse)
    }

    const val SUCCESS_RESPONSE_CODE = 200
    const val NOT_FOUND_RESPONSE_CODE = 404
}

object MockDataUtil {
    const val USER_LOGIN = "user1"
    const val USER_ID = 1
    const val USER_AVATAR_URL = "http://example.com/avatar.jpg"
    val USER = User(USER_LOGIN, USER_ID, USER_AVATAR_URL)
    val EMPTY_LIST = emptyList<String>()
    val REPO = Repo(
        id = 1,
        name = "repo1",
        htmlUrl = "http://example.com/repo1",
        description = "Sample repository",
        fork = false,
        stargazersCount = 100,
        language = "Kotlin",
    )
    val SEARCH_RESULT = SearchResult(
        totalCount = 1,
        incompleteResults = false,
        items = listOf(USER)
    )
    val EMPTY_SEARCH_RESULT = SearchResult(
        totalCount = 0,
        incompleteResults = false,
        items = emptyList()
    )
    val REPO_LIST = listOf(REPO)
    val USER_LIST = listOf(USER)
}

class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
