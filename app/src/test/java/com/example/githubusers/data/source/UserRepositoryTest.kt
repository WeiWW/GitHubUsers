package com.example.githubusers.data.source

import android.util.Log
import androidx.paging.PagingData
import com.example.githubusers.data.modal.Repo
import com.example.githubusers.data.modal.SearchResult
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.modal.UserDetail
import com.example.githubusers.data.remote.GitHubApiService
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserRepositoryTest {
    private val apiService = mockk<GitHubApiService>()
    private val userRepository = UserRepository(apiService)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        coEvery { Log.isLoggable(any(), any()) } returns true
    }

    @Test
    fun `get Users successfully and return PagingData`(): Unit = runBlocking {
        val users = listOf(mockk<User>(), mockk())

        coEvery { apiService.listUsers(any(), any()) } returns Response.success(users)

        val result = userRepository.getUsers().first()

        result shouldBeInstanceOf PagingData::class
    }

    @Test
    fun `search Users successfully and return PagingData`(): Unit = runBlocking {
        val searchResult = mockk<SearchResult>()

        coEvery { apiService.searchUsers(any(), any(),any()) } returns Response.success(searchResult)

        val result = userRepository.searchUsers("query").first()

        result shouldBeInstanceOf PagingData::class
    }

    @Test
    fun `get UserDetail's response successfully and return it`(): Unit = runBlocking {
        val userDetail = mockk<UserDetail>()
        coEvery { apiService.getUser(any()) } returns Response.success(userDetail)

        val result = userRepository.getUser("username").first()

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe userDetail
    }

    @Test
    fun `get UserDetail's response unsuccessfully and return error`(): Unit = runBlocking {
        coEvery { apiService.getUser(any()) } returns Response.error(
            404,
            "Not Found".toResponseBody("application/json".toMediaTypeOrNull())
        )

        val result = userRepository.getUser("username").first()
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message!! shouldNotBe null
    }

    @Test
    fun `get UserDetail's response body is null return error`(): Unit = runBlocking {
        coEvery { apiService.getUser(any()) } returns Response.success(null)

        val result = userRepository.getUser("username").first()
        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBeEqualTo "Error: 200 null"
    }

    @Test
    fun `get User Repos successfully and return PagingData`(): Unit = runBlocking {
        val repos = listOf(mockk<Repo>())

        coEvery { apiService.getUserRepos(any(),any(),any()) } returns Response.success(repos)

        val result = userRepository.getUserRepos("username").first()

        result shouldBeInstanceOf PagingData::class
    }
}