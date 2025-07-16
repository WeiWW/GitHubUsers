package com.example.githubusers.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubusers.data.modal.Repo
import com.example.githubusers.data.remote.GitHubApiService
import retrofit2.HttpException

class RepoPagingSource(private val apiService: GitHubApiService, private val userName: String) :
    PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key ?: 1
            val perPage = params.loadSize
            val response = apiService.getUserRepos(userName, perPage = perPage, page)

            if (response.isSuccessful) {
                val repos = response.body() ?: emptyList()
                LoadResult.Page(
                    data = repos,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (repos.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(HttpException(response))
            }

        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}