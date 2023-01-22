package com.compose.androidtoanime

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING
import com.compose.androidtoanime.screens.DialogExit
import com.compose.androidtoanime.screens.HowToUse
import com.compose.androidtoanime.screens.MyNavigationDrawer
import com.compose.androidtoanime.screens.Premium
import com.compose.androidtoanime.ui.theme.AndroidToAnimeTheme
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.compose.androidtoanime.viewmodels.PricingViewModel
import com.wishes.jetpackcompose.runtime.NavigationHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidToAnimeTheme {

                val viewModel: MainViewModel = hiltViewModel()
                val pricingViewModel: PricingViewModel = hiltViewModel()
                val navController = rememberNavController()
                val context = LocalContext.current
                //pricingViewModel.getProducts()

                val isSubscribe = remember {
                    derivedStateOf { pricingViewModel.isSubscribe.value }
                }

                LaunchedEffect(key1 = Unit) {
                    //pricingViewModel.savePurchase(pricingViewModel.purchaes.value!![0])
                    pricingViewModel.checkSubscription()
                    Log.d(TAG_BILLING, "LaunchedEffect ")
                }

                LaunchedEffect(key1 = pricingViewModel.purchaes.value) {
                    //pricingViewModel.savePurchase(pricingViewModel.purchaes.value!![0])
                    pricingViewModel.checkSubscription()
                    Log.d(TAG_BILLING, "LaunchedEffect ")
                }

                val offSetAnim by animateDpAsState(
                    targetValue = if (viewModel.navigateClick) 300.dp else 0.dp,
                    tween(1000)
                )
                val clipDp by animateDpAsState(
                    targetValue = if (viewModel.navigateClick) 40.dp else 0.dp,
                    tween(1000)
                )
                val scaleAnim by animateFloatAsState(
                    targetValue = if (viewModel.navigateClick) 0.5f else 1.0f,
                    tween(1000)
                )
                val rotate by animateFloatAsState(
                    targetValue = if (viewModel.navigateClick) 6f else 0f,
                    tween(1000)
                )

                MyNavigationDrawer() {
                    viewModel.navigateClick = false
                }
//                Image(
//                    painter = painterResource(id = R.drawable.wallpaper),
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scaleAnim)
                        .offset(x = offSetAnim)
                        .rotate(rotate)
                        .clip(RoundedCornerShape(clipDp))
                ) {
                    NavigationHost(
                        navController = navController,
                        viewModel,
                        pricingViewModel = pricingViewModel
                    )
                }

                if (viewModel.openPremium)
                    Premium(enable = !isSubscribe.value, close = {
                        viewModel.openPremium = false
                    },
                        purchase = {
                            //pricingViewModel.checkSubscription()
                            pricingViewModel.makePurchase(it,
                                (context as Activity)
                            )
                            //pricingViewModel.makePurchase((context as Activity))
                        }
                    )

                if (viewModel.openExit)
                    DialogExit(context) {
                        viewModel.openExit = false
                    }

                if (viewModel.openHow)
                    HowToUse(context) {
                        viewModel.openHow = false
                    }
            }

        }
    }
}




