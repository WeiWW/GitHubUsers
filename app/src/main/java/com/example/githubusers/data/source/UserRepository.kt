package com.example.githubusers.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubusers.data.User
import com.example.githubusers.data.UserDetail
import com.example.githubusers.data.remote.GitHubApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(private val apiService: GitHubApiService) {

    fun getUsers(): Flow<PagingData<User>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UserPagingSource(apiService) }
    ).flow

    fun getUser(userName: String): Flow<Result<UserDetail>> = flow {
        val response = apiService.getUser(userName)
        if (response.isSuccessful && response.body() != null) {
            emit(Result.success(response.body()!!))
        } else {
            emit(
                Result.failure(
                    Exception(
                        "Error: ${response.code()} ${
                            response.errorBody().toString()
                        }"
                    )
                )
            )
        }
    }
}