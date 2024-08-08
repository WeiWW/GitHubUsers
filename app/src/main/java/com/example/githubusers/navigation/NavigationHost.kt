package com.example.githubusers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.githubusers.ui.userDetail.UserDetailScreen
import com.example.githubusers.ui.userList.UserListScreen


@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            UserListScreen(modifier) { userName ->
                navController.navigate("userDetail/$userName")
            }
        }
        composable("userDetail/{userName}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            UserDetailScreen(userName)
        }
    }
}