package com.example.githubusers.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.remote.GitHubApiService
import retrofit2.HttpException
import java.io.IOException

class SearchPagingSource(private val api: GitHubApiService, private val query: String) :
    PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize
            val response = api.searchUsers(query, perPage, page)

            if (response.isSuccessful) {
                val users = response.body()?.items ?: emptyList()
                LoadResult.Page(
                    data = users,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (users.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}