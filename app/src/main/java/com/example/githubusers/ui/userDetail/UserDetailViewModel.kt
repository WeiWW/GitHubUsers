package com.example.githubusers.ui.userDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers.data.UserDetail
import com.example.githubusers.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(private val repository: UserRepository) :
    ViewModel() {
    private val _userDetail = MutableStateFlow<UserDetail?>(null)
    val userDetail: StateFlow<UserDetail?> = _userDetail

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchUserDetail(userName: String) {
        viewModelScope.launch {
            repository.getUser(userName)
                .catch { e ->
                    _error.emit(e.message)
                    _userDetail.emit(null)
                }
                .collect { result ->
                    result.onSuccess { userDetail ->
                        _error.emit(null)
                        _userDetail.emit(userDetail)
                    }.onFailure {
                        _error.emit(it.message)
                        _userDetail.emit(null)
                    }.exceptionOrNull()?.let {
                        _error.emit(it.message)
                        _userDetail.emit(null)
                    }
                }
        }
    }
}