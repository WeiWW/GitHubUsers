package com.example.githubusers.ui.userList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubusers.data.User
import com.example.githubusers.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    private val _refreshTrigger = MutableStateFlow(0)
    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val users: Flow<PagingData<User>> = _refreshTrigger.flatMapLatest {
        repository.getUsers().cachedIn(viewModelScope)
            .catch { e ->
                _error.emit(e)
            }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            try {
                _refreshTrigger.value++
                users.first()
                _error.emit(null)
            } catch (e: Throwable) {
                _error.emit(e)
            } finally {
                _isRefreshing.emit(false)
            }
        }
    }
}