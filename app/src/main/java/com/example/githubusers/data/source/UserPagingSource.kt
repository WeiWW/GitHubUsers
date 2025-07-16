package com.example.githubusers.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.remote.GitHubApiService
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(private val api: GitHubApiService) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val since = params.key ?: 0
            val perPage = params.loadSize
            val response = api.listUsers(since, perPage)

            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                LoadResult.Page(
                    data = users,
                    prevKey = if (since == 0) null else since - perPage,
                    nextKey = if (users.isEmpty()) null else users.last().id
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