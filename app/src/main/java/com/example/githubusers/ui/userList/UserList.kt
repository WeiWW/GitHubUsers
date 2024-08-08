package com.example.githubusers.ui.userList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.githubusers.data.User
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState


@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    onUserClick: (String) -> Unit
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val users = userViewModel.users.collectAsLazyPagingItems()
    val isRefreshing by userViewModel.isRefreshing.collectAsState()
    val swipeRefreshState = remember { SwipeRefreshState(isRefreshing) }

    LaunchedEffect(isRefreshing) {
        swipeRefreshState.isRefreshing = isRefreshing
    }

    Box(modifier = modifier.fillMaxSize()) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { userViewModel.refreshUsers() },
            modifier = modifier.fillMaxSize()
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = users.itemCount,
                    key = { index -> users[index]?.id ?: index }
                ) { index ->
                    users[index]?.let { user ->
                        UserItem(user) {
                            onUserClick(user.login)
                        }
                    }
                }.apply {
                    when {
                        users.loadState.refresh is androidx.paging.LoadState.Loading -> {
                            item {
                                CircularIndicator()
                            }
                        }

                        users.loadState.append is androidx.paging.LoadState.Loading -> {
                            item {
                                CircularIndicator()
                            }
                        }

                        users.loadState.refresh is androidx.paging.LoadState.Error -> {
                            item {
                                ErrorScreen(message = "Error: ${(users.loadState.refresh as androidx.paging.LoadState.Error).error.message}")
                            }
                        }

                        users.loadState.append is androidx.paging.LoadState.Error -> {
                            item {
                                ErrorScreen(message = "Error: ${(users.loadState.append as androidx.paging.LoadState.Error).error.message}")
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val AVATAR_BASE_URL = "https://avatars.githubusercontent.com/u/"

@Composable
fun UserItem(user: User, onClick: () -> Unit) {

    Row(modifier = Modifier
        .padding(8.dp)
        .clickable { onClick() }
    ) {
        SubcomposeAsyncImage(
            model = "$AVATAR_BASE_URL${user.id}",
            contentDescription = "Loaded image",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.Fit,
            loading = {
                Box(Modifier.matchParentSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            },
            error = {
                Text("No Image")
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = user.login,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserItem() {
    val sampleUser = User(
        login = "Sample User",
        id = 1,
        avatarUrl = "https://avatars.githubusercontent.com/u/1"
    )
    UserItem(user = sampleUser, onClick = {})
}

@Composable
fun CircularIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = message,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}