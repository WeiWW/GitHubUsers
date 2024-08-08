package com.example.githubusers.data.remote

import com.example.githubusers.data.User
import com.example.githubusers.data.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {
    @GET("users")
    suspend fun listUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Response<List<User>>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") userName: String): Response<UserDetail>
}