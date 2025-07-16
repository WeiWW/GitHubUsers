package com.example.githubusers.data.source

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubusers.MockDataUtil.REPO
import com.example.githubusers.MockServerUtil
import com.example.githubusers.data.remote.GitHubApiService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test

class RepoPagingSourceTest {
    private lateinit var apiService: GitHubApiService
    private lateinit var pagingSource: RepoPagingSource

    @Before
    fun setUp() {
        MockServerUtil.setUp()
        apiService = MockServerUtil.apiService
        pagingSource = RepoPagingSource(apiService, "testUser")
    }

    @After
    fun tearDown() {
        MockServerUtil.tearDown()
    }

    @Test
    fun `load returns page on successful data load`(): Unit = runBlocking {
        MockServerUtil.enqueueMockResponse(
            MockServerUtil.SUCCESS_RESPONSE_CODE,
            Gson().toJson(listOf(REPO))
        )

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Page::class
        val page = result as PagingSource.LoadResult.Page
        page.data.size shouldBeEqualTo 1
        page.prevKey shouldBeEqualTo -1
        page.nextKey shouldBeEqualTo 1
    }

    @Test
    fun `load returns error on API failure`(): Unit = runBlocking {
        MockServerUtil.enqueueMockResponse(MockServerUtil.NOT_FOUND_RESPONSE_CODE)

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Error::class
    }

    @Test
    fun `load returns error on network failure`(): Unit = runBlocking {
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
                    data = listOf(REPO),
                    prevKey = null,
                    nextKey = 2
                )
            ),
            anchorPosition = 0,
            config = PagingConfig(pageSize = 2),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        refreshKey shouldBeEqualTo 1
    }
}