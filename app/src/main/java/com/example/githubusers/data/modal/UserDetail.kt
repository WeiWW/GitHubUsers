package com.example.githubusers.data.modal

data class UserDetail(
    val login: String,
    val id: Int,
    val avatarUrl: String,
    val url: String,
    val htmlUrl: String,
    val name: String?,
    val followers: Int,
    val following: Int,
)