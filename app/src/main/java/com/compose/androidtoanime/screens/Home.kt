package com.compose.androidtoanime.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.compose.androidtoanime.TopBar
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.ringtones.compose.feature.admob.AdvertViewAdmob
import com.wishes.jetpackcompose.runtime.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                myphoto = {
                    navController.navigate(NavRoutes.MyPhotos.route)
                },
                drawer = {
                    viewModel.navigateClick = true
                },

                ) {
                viewModel.openPremium = true
            }
        },
        bottomBar = {
            AdvertViewAdmob()
        }
    )
    {
        Column(modifier = Modifier.padding(it)) {
            //NavigationHost(navController, viewModel)
            Upload(navController = navController, viewModel = viewModel)
        }
    }
}