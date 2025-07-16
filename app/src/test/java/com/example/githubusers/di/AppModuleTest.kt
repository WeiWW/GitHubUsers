package com.example.githubusers.di

import com.example.githubusers.data.remote.GitHubApiService
import com.example.githubusers.data.source.UserRepository
import io.mockk.mockk
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test

class AppModuleTest {

    @Test
    fun `provideOkHttpClient returns OkHttpClient with AuthInterceptor`() {
        val client = AppModule.provideOkHttpClient()
        client.interceptors.any { it is AuthInterceptor } shouldBeInstanceOf Boolean::class
    }

    @Test
    fun `provideGitHubApiService returns GitHubApiService instance`() {
        val service = AppModule.provideGitHubApiService()
        service.shouldNotBeNull()
        service shouldBeInstanceOf GitHubApiService::class
    }

    @Test
    fun `provideUserRepository returns UserRepository with correct apiService`() {
        val mockApiService = mockk<GitHubApiService>()
        val repository = AppModule.provideUserRepository(mockApiService)
        repository.shouldNotBeNull()
        repository shouldBeInstanceOf UserRepository::class
    }
}