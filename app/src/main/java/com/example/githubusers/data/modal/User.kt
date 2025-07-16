package com.example.githubusers.data.modal

data class User(
    val login: String,
    val id: Int,
    val avatarUrl: String
)

data class SearchResult(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<User>
)
