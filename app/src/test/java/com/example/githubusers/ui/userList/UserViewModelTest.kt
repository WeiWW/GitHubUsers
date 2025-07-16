package com.example.githubusers.ui.userList

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.example.githubusers.MockDataUtil.USER_LIST
import com.example.githubusers.NoopListCallback
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.source.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val pagingDataDiffer = AsyncPagingDataDiffer(
        diffCallback = diffCallback(),
        updateCallback = NoopListCallback(),
        workerDispatcher = testDispatcher
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        viewModel = UserViewModel(userRepository)
    }

    @After
    fun tearDown() {
        pagingDataDiffer.refresh()
        Dispatchers.resetMain()
    }

    @Test
    fun `get user list successfully`() = runTest {
        coEvery { userRepository.getUsers() } returns flow { emit(PagingData.from(USER_LIST)) }

        val job = launch{
            viewModel.users.collectLatest {
                pagingDataDiffer.submitData(it)
            }
        }
        advanceUntilIdle()
        pagingDataDiffer.snapshot().items shouldContainAll USER_LIST
        job.cancel()
    }

    @Test
    fun `get user list, response with exception`() = runTest {
        val errorMessage = "Network error"
        coEvery { userRepository.getUsers() } throws Exception(errorMessage)

        val job = launch {
            viewModel.users.collectLatest {
                pagingDataDiffer.submitData(it)
            }
        }
        advanceUntilIdle()

        pagingDataDiffer.snapshot().items.size shouldBeEqualTo 0
        job.cancel()
    }
    @Test
    fun `should emit searched users on success`() = runTest {
        coEvery { userRepository.searchUsers(any()) } returns flow { emit(PagingData.from(USER_LIST)) }
        viewModel.setSearchQuery("user")
        val job = launch {
            viewModel.users.collect {
                pagingDataDiffer.submitData(it)
            }
        }
        advanceUntilIdle()
        pagingDataDiffer.snapshot().items shouldContainAll USER_LIST
        job.cancel()
    }

    @Test
    fun `refresh users successfully`() = runTest {
        coEvery { userRepository.getUsers() } returns flow { emit(PagingData.from(USER_LIST)) }

        val job = launch {
            viewModel.users.collectLatest {
                pagingDataDiffer.submitData(it)
            }
        }
        advanceUntilIdle()

        pagingDataDiffer.snapshot().items shouldContainAll USER_LIST

        viewModel.refreshUsers()
        advanceUntilIdle()

        pagingDataDiffer.snapshot().items shouldContainAll USER_LIST
        coVerify(exactly = 2) { userRepository.getUsers() }
        job.cancel()
    }

    @Test
    fun `refresh successfully with search query`() = runTest {
        coEvery { userRepository.searchUsers(any()) } returns flow { emit(PagingData.from(USER_LIST)) }

        viewModel.setSearchQuery("user")
        val job = launch {
            viewModel.users.collectLatest {
                pagingDataDiffer.submitData(it)
            }
        }
        advanceUntilIdle()

        pagingDataDiffer.snapshot().items shouldContainAll USER_LIST

        viewModel.refreshUsers()
        advanceUntilIdle()

        pagingDataDiffer.snapshot().items shouldContainAll USER_LIST
        coVerify(exactly = 2) { userRepository.searchUsers("user") }
        job.cancel()
    }

    private fun diffCallback() = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}