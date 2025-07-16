package com.example.githubusers.ui.userList

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.githubusers.R
import com.example.githubusers.data.modal.User
import com.example.githubusers.data.source.UserRepository.Companion.AVATAR_BASE_URL
import com.example.githubusers.ui.Utils.AVATAR
import com.example.githubusers.ui.Utils.USER


@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "${AVATAR_BASE_URL}${user.id}",
                    error = painterResource(id = R.drawable.ic_default_avatar),
                    placeholder = painterResource(id = R.drawable.ic_default_avatar)
                ),
                contentDescription = AVATAR,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "@${user.login}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview()
@Composable
fun PreviewUserCard() {
    UserCard(
        user = USER,
        onClick = {}
    )
}