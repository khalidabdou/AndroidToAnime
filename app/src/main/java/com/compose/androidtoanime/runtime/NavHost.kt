package com.wishes.jetpackcompose.runtime

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compose.androidtoanime.screens.*
import com.compose.androidtoanime.viewmodels.ViewModel



@Composable
fun NavigationHost(navController: NavHostController,viewModel: ViewModel) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
    ) {
        composable(NavRoutes.Splash.route) {
            Splash(navController,viewModel)
        }

        composable(NavRoutes.Home.route) {
            Home(navController,viewModel)
        }

        composable(NavRoutes.Upload.route) {
            Upload(navController,viewModel)
        }

        composable(NavRoutes.Share.route) {
            Share(viewModel)
        }

        composable(NavRoutes.MyPhotos.route) {
            MyPhotos(viewModel)
        }
    }
}
