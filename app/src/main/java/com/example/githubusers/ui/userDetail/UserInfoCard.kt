package com.example.githubusers.ui.userDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.githubusers.R
import com.example.githubusers.data.modal.UserDetail
import com.example.githubusers.data.source.UserRepository.Companion.AVATAR_BASE_URL
import com.example.githubusers.ui.Utils.AVATAR
import com.example.githubusers.ui.Utils.USER_DETAIL


@Composable
fun UserInfoCard(userDetail: UserDetail?) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "${AVATAR_BASE_URL}${userDetail?.id}",
                    error = painterResource(id = R.drawable.ic_default_avatar),
                    placeholder = painterResource(id = R.drawable.ic_default_avatar),
                ),
                contentDescription = AVATAR,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = userDetail?.name ?: "No Name",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "@${userDetail?.login ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Followers",
                    tint = Color.Gray
                )
                Text(
                    text = "Followers: ${userDetail?.followers}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Following",
                    tint = Color.Gray
                )
                Text(
                    text = "Following: ${userDetail?.following}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview()
@Composable
fun UserInfoCardPreview() {
    UserInfoCard(
        userDetail = USER_DETAIL
    )
}