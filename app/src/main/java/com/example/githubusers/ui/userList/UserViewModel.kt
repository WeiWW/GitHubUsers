package com.example.githubusers.ui.userList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 0)
    private val _searchQuery = MutableStateFlow<String?>(null)
    val users: Flow<PagingData<User>> =
        combine(_searchQuery, _refreshTrigger.onStart { emit(Unit) }) { query, _ ->
            query
        }.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getUsers()
        } else {
            repository.searchUsers(query)
        }
    }.catch { e ->
        Log.e("UserViewModel", "Error fetching users", e)
    }.cachedIn(viewModelScope)

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _refreshTrigger.emit(Unit)
        }
    }
}