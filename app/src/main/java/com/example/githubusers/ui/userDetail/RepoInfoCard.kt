package com.example.githubusers.ui.userDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubusers.data.modal.Repo
import com.example.githubusers.ui.Utils.REPO


@Composable
fun RepoInfoCard(repo: Repo?,openLink: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .clickable(enabled = repo?.htmlUrl != null) {
                repo?.htmlUrl?.let { openLink(it) }
            }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = repo?.name ?: "No Name",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Stars",
                    tint = Color(0xFFFFD700)
                )
                Text(
                    text = " ${repo?.stargazersCount ?: 0}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = repo?.language ?: "N/A",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = repo?.description ?: "No Description",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = "url: ${repo?.htmlUrl ?: ""}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun RepoInfoCardPreview() {
    RepoInfoCard(
        repo = REPO,
        openLink = {}
    )
}