package com.compose.androidtoanime.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.Connexion
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.Utils.animateVisibility
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

    var isOnline = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val connexion = Connexion()
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


    isOnline.value = connexion.isOnline(context)
    if (!isOnline.value) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = painterResource(id = R.drawable.no_wifi), contentDescription = "no_wifi")
            Text(text = "No Internet Connexion")
            Spacer(modifier = Modifier.height(6.dp))
            Button(onClick = {
                isOnline.value = connexion.isOnline(context)
                if (!isOnline.value)
                    Toast.makeText(context, "no internet connexion", Toast.LENGTH_LONG).show()

            }) {
                Text(text = "Check again")
            }
        }
        return
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

        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = null
        ).animateVisibility()

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



