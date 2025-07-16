package com.example.githubusers.ui.userDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubusers.data.modal.UserDetail
import com.example.githubusers.data.source.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(private val repository: UserRepository) :
    ViewModel() {
    private val _userDetail = MutableStateFlow<UserDetail?>(null)
    val userDetail: StateFlow<UserDetail?> = _userDetail

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    @OptIn(ExperimentalCoroutinesApi::class)
    val repos = _userDetail
        .asStateFlow()
        .flatMapLatest { userDetail ->
            if (userDetail?.login != null) {
                repository.getUserRepos(userDetail.login)
            } else {
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)
        .catch { it.message?.let { msg -> _error.value = msg } }

    fun fetchUserDetail(userName: String) {
        viewModelScope.launch {
            repository
                .getUser(userName)
                .catch { e ->
                    _error.value = e.message
                    _userDetail.value = null
                }
                .collectLatest { result ->
                    result.onSuccess { userDetail ->
                        _error.value = null
                        _userDetail.value = userDetail
                    }.onFailure {
                        _error.value = it.message
                        _userDetail.value = null
                    }.exceptionOrNull()?.let {
                        _error.value = it.message
                        _userDetail.value = null
                    }
                }
        }
    }

    val uiState: StateFlow<UserDetailUiState> = combine(
        userDetail,
        error
    ) { user, err ->
        UserDetailUiState(user, err)
    }.stateIn(viewModelScope, SharingStarted.Lazily, UserDetailUiState(null, null))
}