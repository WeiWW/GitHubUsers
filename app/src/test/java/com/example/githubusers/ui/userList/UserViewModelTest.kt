package com.example.githubusers.ui.userList

import androidx.paging.PagingData
import com.example.githubusers.data.User
import com.example.githubusers.data.source.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        userViewModel = UserViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `users fetches data successfully`() = runTest {
        val pagingData = PagingData.from(listOf(mockk<User>()))
        coEvery { userRepository.getUsers() } returns flow { emit(pagingData) }

        val result = userViewModel.users.first()
        advanceUntilIdle()

        result shouldBeInstanceOf PagingData::class
        userViewModel.error.value shouldBe null
    }

    @Test
    fun `users fetches data with error`() = runTest {
        val errorMessage = "Network error"
        coEvery { userRepository.getUsers() } throws Exception(errorMessage)

        userViewModel.refreshUsers()
        advanceUntilIdle()

        userViewModel.isRefreshing.value shouldBe false
        userViewModel.error.value?.message shouldBeEqualTo errorMessage
    }

    @Test
    fun `refreshUsers updates isRefreshing and error on success`() = runTest {
        val pagingData = PagingData.from(listOf(mockk<User>()))
        coEvery { userRepository.getUsers() } returns flow { emit(pagingData) }

        userViewModel.refreshUsers()
        advanceUntilIdle()

        userViewModel.isRefreshing.value shouldBe false
        userViewModel.error.value shouldBe null
    }

    @Test
    fun `refreshUsers updates isRefreshing and error on failure`() = runTest {
        val errorMessage = "Network error"
        coEvery { userRepository.getUsers() } throws Exception(errorMessage)

        userViewModel.refreshUsers()
        advanceUntilIdle()

        userViewModel.isRefreshing.value shouldBeEqualTo false
        userViewModel.error.value?.message shouldBeEqualTo errorMessage
    }
}