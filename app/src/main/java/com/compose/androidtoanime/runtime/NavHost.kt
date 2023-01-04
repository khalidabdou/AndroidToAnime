package com.wishes.jetpackcompose.runtime

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compose.androidtoanime.screens.Share
import com.compose.androidtoanime.screens.Upload
import com.compose.androidtoanime.viewmodels.ViewModel



@Composable
fun NavigationHost(navController: NavHostController,viewModel: ViewModel) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
    ) {

        composable(NavRoutes.Upload.route) {
            Upload(navController,viewModel)
        }

        composable(NavRoutes.Share.route) {
            Share(navController,viewModel)
        }
    }
}