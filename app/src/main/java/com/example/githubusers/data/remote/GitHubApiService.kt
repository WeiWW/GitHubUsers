package com.example.githubusers.data.remote

import com.example.githubusers.data.modal.Repo
import com.example.githubusers.data.modal.SearchResult
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.modal.UserDetail
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

    @GET(value = "users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") userName: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<List<Repo>>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<SearchResult>
}