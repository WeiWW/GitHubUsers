package com.example.githubusers.ui.userDetail

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.example.githubusers.MockDataUtil.REPO_LIST
import com.example.githubusers.MockDataUtil.USER_LOGIN
import com.example.githubusers.NoopListCallback
import com.example.githubusers.data.modal.Repo
import com.example.githubusers.data.modal.UserDetail
import com.example.githubusers.data.source.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: UserDetailViewModel
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
        viewModel = UserDetailViewModel(userRepository)
    }

    @After
    fun tearDown() {
        pagingDataDiffer.refresh()
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUserDetail updates userDetail and user's repos on success`() = runTest {
        val userDetail = mockk<UserDetail> { coEvery { login } returns USER_LOGIN }
        coEvery { userRepository.getUser(USER_LOGIN) } returns flow { emit(Result.success(userDetail)) }
        coEvery { userRepository.getUserRepos(USER_LOGIN) } returns flowOf(PagingData.from(REPO_LIST))

        viewModel.fetchUserDetail(USER_LOGIN)
        advanceUntilIdle()

        val job = launch {
            viewModel.repos.collectLatest { pagingDataDiffer.submitData(it) }
            viewModel.uiState.collectLatest {
                it.userDetail shouldBeEqualTo userDetail
                it.error shouldBe null
            }
        }
        advanceUntilIdle()

        pagingDataDiffer.snapshot().items shouldContainAll REPO_LIST
        viewModel.userDetail.value shouldBe userDetail
        viewModel.error.value shouldBe null
        coVerify(exactly = 1) { userRepository.getUserRepos(USER_LOGIN) }

        job.cancel()
    }

    @Test
    fun `fetchUserDetail updates error on failure and user's repo is empty`() = runTest {
        val errorMessage = "User not found"
        coEvery { userRepository.getUser(USER_LOGIN) } returns flow {
            emit(Result.failure(Exception(errorMessage)))
        }

        viewModel.fetchUserDetail(USER_LOGIN)
        advanceUntilIdle()

        val job = launch {
            viewModel.repos.collectLatest {
                pagingDataDiffer.submitData(it)
            }
            viewModel.uiState.collectLatest {
                it.userDetail shouldBe null
                it.error shouldBeEqualTo errorMessage
            }
        }
        advanceUntilIdle()
        pagingDataDiffer.snapshot().items.size shouldBeEqualTo 0
        viewModel.userDetail.value shouldBe null
        viewModel.error.value shouldBeEqualTo errorMessage
        coVerify(exactly = 0) { userRepository.getUserRepos(any()) }
        job.cancel()
    }

    @Test
    fun `fetchUserDetail updates catch exception and user's repos is empty`() = runTest {
        val errorMessage = "Exception"
        coEvery { userRepository.getUser(USER_LOGIN) } returns flow {
            throw Exception(errorMessage)
        }

        viewModel.fetchUserDetail(USER_LOGIN)
        advanceUntilIdle()

        val job = launch {
            viewModel.repos.collectLatest {
                pagingDataDiffer.submitData(it)
            }
            viewModel.uiState.collectLatest {
                it.userDetail shouldBe null
                it.error shouldBeEqualTo errorMessage
            }
        }
        advanceUntilIdle()
        pagingDataDiffer.snapshot().items.size shouldBeEqualTo 0
        viewModel.userDetail.value shouldBe null
        viewModel.error.value shouldBeEqualTo errorMessage
        coVerify(exactly = 0) { userRepository.getUserRepos(any()) }
        job.cancel()
    }

    private fun diffCallback() = object : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
    }

}