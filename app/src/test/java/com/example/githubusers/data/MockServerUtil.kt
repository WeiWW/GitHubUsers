package com.example.githubusers.data

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
    const val USER_LOGIN = "user1"
    const val USER_ID = 1
    const val USER_AVATAR_URL = "http://example.com/avatar.jpg"
    val USER = User(USER_LOGIN, USER_ID, USER_AVATAR_URL)
    val EMPTY_LIST = emptyList<String>()
}