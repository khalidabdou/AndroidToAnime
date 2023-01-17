package com.compose.androidtoanime.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.compose.androidtoanime.viewmodels.PricingViewModel

import com.wishes.jetpackcompose.runtime.NavRoutes

import kotlinx.coroutines.delay


@Composable
fun Splash(
    navController: NavHostController,
    viewModel: MainViewModel,
    pricingViewModel: PricingViewModel
) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    if (pricingViewModel.productList.isEmpty()) {
        pricingViewModel.getProducts()
    }


    if (viewModel.infos.value is NetworkResults.Loading) {
        viewModel.getAds()
    } else if (viewModel.infos.value is NetworkResults.Error) {
        LaunchedEffect(key1 = true) {
            startAnimation = true
            delay(2000)
            navController.popBackStack()
            navController.navigate(NavRoutes.Home.route)
            //Toast.makeText(context, "${viewModel.adsList.value}", Toast.LENGTH_LONG).show()
        }
    } else {
        LaunchedEffect(key1 = true) {
            startAnimation = true
            delay(2000)
            navController.popBackStack()
            navController.navigate(NavRoutes.Home.route)
            //Toast.makeText(context, "${viewModel.adsList.value}", Toast.LENGTH_LONG).show()
        }
    }

    MSplash(alpha = alphaAnim.value)
}

@Composable
fun MSplash(alpha: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.subtitle),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

