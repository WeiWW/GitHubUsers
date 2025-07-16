package com.example.githubusers.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.githubusers.ui.componets.WebViewScreen
import com.example.githubusers.ui.userDetail.UserDetailScreen
import com.example.githubusers.ui.userList.UserListScreen

sealed class Screen(val route: String) {
    object UserList : Screen("userList")

    object UserDetail : Screen("userDetail/{userName}") {
        const val USER_NAME_ARG = "userName"
        fun createRoute(userName: String) = "userDetail/$userName"
    }

    object WebView : Screen("webview/{url}") {
        const val URL_ARG = "url"
        fun createRoute(url: String) = "webview/${Uri.encode(url)}"
    }
}

@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Screen.UserList.route) {
        composable(Screen.UserList.route) {
            UserListScreen(modifier) { userName ->
                navController.navigate(Screen.UserDetail.createRoute(userName))
            }
        }
        composable(Screen.UserDetail.route) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString(Screen.UserDetail.USER_NAME_ARG).orEmpty()
            UserDetailScreen(userName) { link ->
                navController.navigate(Screen.WebView.createRoute(link))
            }
        }
        composable(Screen.WebView.route) { backStackEntry ->
            val url =
                backStackEntry.arguments?.getString(Screen.WebView.URL_ARG)?.let { Uri.decode(it) }
                    .orEmpty()
            WebViewScreen(url)
        }
    }
}