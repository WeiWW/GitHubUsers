package com.example.githubusers.ui.userDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubusers.data.modal.UserDetail
import com.example.githubusers.ui.componets.ErrorBox

@Composable
fun UserDetailScreen(userName: String, openLink: (String) -> Unit) {
    val viewModel: UserDetailViewModel = hiltViewModel()
    LaunchedEffect(userName) { viewModel.fetchUserDetail(userName) }
    val uiState by viewModel.uiState.collectAsState()
    val repos = viewModel.repos.collectAsLazyPagingItems()

    if (uiState.userDetail == null && uiState.error != null) {
        ErrorBox("Error: ${uiState.error ?: "Unknown"}", Modifier.fillMaxWidth())
    } else {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            UserInfoCard(uiState.userDetail)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Repositories",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(repos.itemCount) { index ->
                    val repo = repos[index]
                    RepoInfoCard(repo, openLink)
                }
            }
        }
    }
}

data class UserDetailUiState(
    val userDetail: UserDetail?,
    val error: String?
)