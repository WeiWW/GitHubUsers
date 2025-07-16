package com.example.githubusers.ui.userList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    onUserClick: (String) -> Unit
) {
    val viewModel: UserViewModel = hiltViewModel()
    val users = viewModel.users.collectAsLazyPagingItems()
    val isRefreshing = users.loadState.source.isIdle.not()
    val pullToRefreshState = rememberPullToRefreshState()
    var searchKeyWord by rememberSaveable { mutableStateOf("") }

    Box(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize()) {
        PullToRefreshBox(
            state = pullToRefreshState,
            onRefresh = { viewModel.refreshUsers() },
            isRefreshing = isRefreshing,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            Column {
                Text(
                    text = "GitHub Users",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = searchKeyWord,
                    onValueChange = { newText ->
                        searchKeyWord = newText
                        viewModel.setSearchQuery(newText)
                    },
                    label = { Text("Search") },
                    placeholder = { Text("Enter your keywords") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        count = users.itemCount,
                        key = { index -> users[index]?.login ?: index }
                    ) { index ->
                        users[index]?.let { user ->
                            UserCard(user = user, onClick = { onUserClick(user.login) })
                        }
                    }
                }
            }
        }
    }
}