package com.compose.androidtoanime.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta.copy(0.1f)),
        topBar = {
            TopBar(
                myphoto = {
                    navController.navigate(NavRoutes.MyPhotos.route)
                },
                drawer = {
                    viewModel.navigateClick = true
                },

                ) {
                navController.navigate(NavRoutes.Premium.route)
                //viewModel.openPremium = true
            }
        },
        bottomBar = {
            if (!pricingViewModel.isSubscribe.value)
                AdvertViewAdmob()
        }
    )
    {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            //NavigationHost(navController, viewModel)
            Upload(
                navController = navController,
                viewModel = viewModel,
                pricingViewModel = pricingViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    myphoto: () -> Unit,
    drawer: () -> Unit,
    open: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
//            Text(text = stringResource(id = R.string.app_name), modifier = Modifier.clickable {
//                drawer()
//            })
        },
        navigationIcon = {
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(27.dp)
                    .padding(2.dp)
                    .clickable {
                        //open()
                        drawer()
                    }
            )
            Spacer(modifier = Modifier.width(3.dp))
        },
        actions = {

            Icon(
                painter = painterResource(id = R.drawable.premium),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(27.dp)
                    .padding(2.dp)
                    .clickable {
                        open()
                    }
            )

            Spacer(modifier = Modifier.width(6.dp))
//            Icon(
//                painter = painterResource(id = R.drawable.photos),
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier
//                    .size(27.dp)
//                    .padding(4.dp)
//                    .clickable {
//                        myphoto()
//                    }
//            )
        }
    )
}

