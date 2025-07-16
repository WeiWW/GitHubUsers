package com.example.githubusers.di

import com.example.githubusers.BuildConfig
import com.example.githubusers.data.remote.GitHubApiService
import com.example.githubusers.data.source.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val URL = "https://api.github.com/"
    private const val TOKEN = BuildConfig.GITHUB_TOKEN

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(TOKEN))
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApiService(): GitHubApiService {
        return Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: GitHubApiService): UserRepository {
        return UserRepository(apiService)
    }

}

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}