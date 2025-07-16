package com.example.githubusers.data.source

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubusers.MockDataUtil.EMPTY_LIST
import com.example.githubusers.MockDataUtil.USER
import com.example.githubusers.MockServerUtil
import com.example.githubusers.MockServerUtil.NOT_FOUND_RESPONSE_CODE
import com.example.githubusers.MockServerUtil.SUCCESS_RESPONSE_CODE
import com.example.githubusers.data.remote.GitHubApiService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserPagingSourceTest {
    private lateinit var apiService: GitHubApiService
    private lateinit var pagingSource: UserPagingSource

    @Before
    fun setUp() {
        MockServerUtil.setUp()
        apiService = MockServerUtil.apiService
        pagingSource = UserPagingSource(apiService)
    }

    @After
    fun tearDown() {
        MockServerUtil.tearDown()
    }

    @Test
    fun `load returns page on successful data load`(): Unit = runBlocking {

        MockServerUtil.enqueueMockResponse(SUCCESS_RESPONSE_CODE, Gson().toJson(listOf(USER)))

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Page::class
        val page = result as PagingSource.LoadResult.Page
        page.data.size shouldBeEqualTo 1
        page.prevKey shouldBeEqualTo null
        page.nextKey shouldBeEqualTo 1
    }

    @Test
    fun `load returns empty page on successful empty data load`(): Unit = runBlocking {

        MockServerUtil.enqueueMockResponse(SUCCESS_RESPONSE_CODE, EMPTY_LIST.toString())

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Page::class
        val page = result as PagingSource.LoadResult.Page
        page.data.size shouldBeEqualTo 0
        page.prevKey shouldBeEqualTo null
        page.nextKey shouldBeEqualTo null
    }

    @Test
    fun `load returns error on API failure`() = runBlocking {

        MockServerUtil.enqueueMockResponse(NOT_FOUND_RESPONSE_CODE)

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Error::class
    }

    @Test
    fun `load returns error on network failure`() = runBlocking {
        MockServerUtil.mockWebServer.shutdown()

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Error::class
    }

    @Test
    fun `getRefreshKey returns correct key`() {
        val state = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = listOf(USER),
                    prevKey = null,
                    nextKey = 2
                )
            ),
            anchorPosition = 1,
            config = PagingConfig(pageSize = 2),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        key shouldBeEqualTo 1
    }

}