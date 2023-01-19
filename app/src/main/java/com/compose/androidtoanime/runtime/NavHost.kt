package com.wishes.jetpackcompose.runtime

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.compose.androidtoanime.screens.*
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.compose.androidtoanime.viewmodels.PricingViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(navController: NavHostController, viewModel: MainViewModel,pricingViewModel: PricingViewModel) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
    ) {
        composable(NavRoutes.Splash.route) {
            Splash(navController, viewModel,pricingViewModel)
        }

        composable(NavRoutes.Home.route) {
            Home(navController, viewModel, pricingViewModel)
        }

        composable(NavRoutes.Upload.route) {
            Upload(navController, viewModel, pricingViewModel)
        }

        composable(NavRoutes.Share.route) {
            Share(viewModel, pricingViewModel)
        }

        composable(NavRoutes.MyPhotos.route) {
            MyPhotos(viewModel)
        }
        composable(NavRoutes.Premium.route) {
            Premium(navController, pricingViewModel)
        }
    }
}
