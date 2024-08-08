package com.example.githubusers.ui.userDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage

private const val AVATAR_BASE_URL = "https://avatars.githubusercontent.com/u/"

@Composable
fun UserDetailScreen(userName: String) {
    val viewModel: UserDetailViewModel = hiltViewModel()

    LaunchedEffect(userName) {
        viewModel.fetchUserDetail(userName)
    }

    val userDetail by viewModel.userDetail.collectAsState()
    val error by viewModel.error.collectAsState()

    if (error != null || userDetail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error: ${error ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    } else {
        userDetail?.let {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                SubcomposeAsyncImage(
                    model = "$AVATAR_BASE_URL${it.id}",
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(Modifier.matchParentSize()) {
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        }
                    },
                    error = {
                        Text("No Image")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it.name ?: "No Name", style = MaterialTheme.typography.headlineLarge)
                Text(text = "@${it.login}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.bio ?: "No Bio", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Company: ${it.company ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Location: ${it.location ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Email: ${it.email ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Twitter: ${it.twitterUsername ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Followers: ${it.followers}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Following: ${it.following}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Public Repos: ${it.publicRepos}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Public Gists: ${it.publicGists}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}