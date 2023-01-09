package com.compose.androidtoanime.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.viewmodels.ViewModel

import com.wishes.jetpackcompose.runtime.NavRoutes

import kotlinx.coroutines.delay


@Composable
fun Splash(navController: NavHostController, viewModel: ViewModel) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )


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

    MSplash(alpha = alphaAnim.value,)
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
            modifier = Modifier.size(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .alpha(alpha),
            painter = painterResource(id = R.drawable.magic),
            contentDescription = "Logo Icon",
        )
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

