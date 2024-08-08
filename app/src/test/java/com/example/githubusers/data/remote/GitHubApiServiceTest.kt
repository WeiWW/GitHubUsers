package com.example.githubusers.data.remote

import com.example.githubusers.data.MockServerUtil
import com.example.githubusers.data.MockServerUtil.EMPTY_LIST
import com.example.githubusers.data.MockServerUtil.NOT_FOUND_RESPONSE_CODE
import com.example.githubusers.data.MockServerUtil.SUCCESS_RESPONSE_CODE
import com.example.githubusers.data.MockServerUtil.USER
import com.example.githubusers.data.MockServerUtil.USER_LOGIN
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test

class GitHubApiServiceTest {
    private lateinit var apiService: GitHubApiService

    @Before
    fun setUp() {
        MockServerUtil.setUp()
        apiService = MockServerUtil.apiService
    }

    @After
    fun tearDown() {
        MockServerUtil.tearDown()
    }

    @Test
    fun `listUsers returns list of users on success`(): Unit = runBlocking {

        val jsonString = Gson().toJson(listOf(USER))
        MockServerUtil.enqueueMockResponse(SUCCESS_RESPONSE_CODE, jsonString)

        val response = apiService.listUsers(0, 2)

        response.code() `should be equal to` SUCCESS_RESPONSE_CODE
        response.body() shouldNotBeEqualTo null
        response.body()?.size `should be equal to` listOf(USER).size
    }

    @Test
    fun `listUsers returns empty list on success with no users`(): Unit = runBlocking {
        MockServerUtil.enqueueMockResponse(SUCCESS_RESPONSE_CODE, EMPTY_LIST.toString())

        val response = apiService.listUsers(0, 2)

        response.code() `should be equal to` SUCCESS_RESPONSE_CODE
        response.body() shouldNotBeEqualTo null
        response.body()?.size `should be equal to` EMPTY_LIST.size
    }

    @Test
    fun `listUsers returns error on failure`(): Unit = runBlocking {

        MockServerUtil.enqueueMockResponse(NOT_FOUND_RESPONSE_CODE)

        val response = apiService.listUsers(0, 2)

        response.code() `should be equal to` NOT_FOUND_RESPONSE_CODE
        response.body() shouldBeEqualTo null
        response.errorBody() shouldNotBeEqualTo null
    }

    @Test
    fun `getUser returns user detail on success`(): Unit = runBlocking {

        MockServerUtil.enqueueMockResponse(SUCCESS_RESPONSE_CODE, Gson().toJson(USER))

        val response = apiService.getUser("user1")

        response.code() `should be equal to` SUCCESS_RESPONSE_CODE
        response.body() shouldNotBeEqualTo null
        response.body()?.login `should be equal to` USER_LOGIN
    }

    @Test
    fun `getUser returns error on failure`(): Unit = runBlocking {

        MockServerUtil.enqueueMockResponse(NOT_FOUND_RESPONSE_CODE)

        val response = apiService.getUser("user1")

        response.code() `should be equal to` NOT_FOUND_RESPONSE_CODE
        response.body() shouldBeEqualTo null
        response.errorBody() shouldNotBeEqualTo null
    }

}