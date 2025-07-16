package com.example.githubusers.data.modal

import com.google.gson.annotations.SerializedName

data class Repo(
    val id: Int,
    val name: String,
    val fork: Boolean,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    val language: String? = null,
    val description: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String,
)