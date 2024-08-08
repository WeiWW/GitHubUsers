package com.example.githubusers.ui.userDetail

import com.example.githubusers.data.UserDetail
import com.example.githubusers.data.source.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userDetailViewModel: UserDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        userDetailViewModel = UserDetailViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUserDetail updates userDetail on success`() = runTest {
        val userDetail = mockk<UserDetail>()
        coEvery { userRepository.getUser("user1") } returns flow {
            emit(Result.success(userDetail))
        }

        userDetailViewModel.fetchUserDetail("user1")
        advanceUntilIdle()

        userDetailViewModel.userDetail.value shouldBe userDetail
        userDetailViewModel.error.value shouldBe null
    }

    @Test
    fun `fetchUserDetail updates error on failure`() = runTest {
        val errorMessage = "User not found"
        coEvery { userRepository.getUser("user1") } returns flow {
            emit(Result.failure(Exception(errorMessage)))
        }

        userDetailViewModel.fetchUserDetail("user1")
        advanceUntilIdle()

        userDetailViewModel.userDetail.value shouldBe null
        userDetailViewModel.error.value shouldBeEqualTo errorMessage
    }
}