package com.example.githubusers.data.source

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubusers.MockDataUtil.EMPTY_SEARCH_RESULT
import com.example.githubusers.MockDataUtil.SEARCH_RESULT
import com.example.githubusers.MockServerUtil
import com.example.githubusers.MockServerUtil.SUCCESS_RESPONSE_CODE
import com.example.githubusers.data.remote.GitHubApiService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchPagingSourceTest {
    private lateinit var apiService: GitHubApiService
    private lateinit var pagingSource: SearchPagingSource

    @Before
    fun setUp() {
        MockServerUtil.setUp()
        apiService = MockServerUtil.apiService
        pagingSource = SearchPagingSource(apiService, "testQuery")
    }

    @After
    fun tearDown() {
        MockServerUtil.tearDown()
    }

    @Test
    fun `load returns page on successful data load`(): Unit = runBlocking {
        MockServerUtil.enqueueMockResponse(
            SUCCESS_RESPONSE_CODE,
            Gson().toJson(SEARCH_RESULT)
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
    fun `load return empty page on successful empty data load`(): Unit = runBlocking {
        MockServerUtil.enqueueMockResponse(
            SUCCESS_RESPONSE_CODE,
            Gson().toJson(EMPTY_SEARCH_RESULT)
        )

        val params =
            PagingSource.LoadParams.Refresh(key = 0, loadSize = 2, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        result shouldBeInstanceOf PagingSource.LoadResult.Page::class
        val page = result as PagingSource.LoadResult.Page
        page.data.size shouldBeEqualTo 0
        page.prevKey shouldBeEqualTo -1
        page.nextKey shouldBeEqualTo null
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
                    data = listOf(SEARCH_RESULT.items.first()),
                    prevKey = null,
                    nextKey = 2
                )
            ),
            anchorPosition = 1,
            config = PagingConfig(pageSize = 2, enablePlaceholders = false),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        key shouldBeEqualTo 1
    }
}