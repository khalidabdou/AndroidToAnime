package com.compose.androidtoanime.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.compose.androidtoanime.viewmodels.PricingViewModel
import com.ringtones.compose.feature.admob.AdvertViewAdmob
import com.wishes.jetpackcompose.runtime.NavRoutes

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavHostController,
    viewModel: MainViewModel,
    pricingViewModel: PricingViewModel
) {
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
            if (!pricingViewModel.isSubscribe.value)
                AdvertViewAdmob()
        }
    )
    {
        Column(modifier = Modifier.padding(it)) {
            //NavigationHost(navController, viewModel)
            Upload(
                navController = navController,
                viewModel = viewModel,
                pricingViewModel = pricingViewModel
            )
        }
    }
}