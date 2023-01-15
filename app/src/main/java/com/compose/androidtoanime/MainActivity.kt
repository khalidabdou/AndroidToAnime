package com.compose.androidtoanime

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidToAnimeTheme {

                val viewModel: MainViewModel = hiltViewModel()
                val pricingViewModel: PricingViewModel = hiltViewModel()
                val navController = rememberNavController()
                val context = LocalContext.current


                pricingViewModel.getProducts()

                if (pricingViewModel.purches.value.isNullOrEmpty()) {
                    Log.d(TAG_BILLING, "null")
                } else {
                    pricingViewModel.savePurchase(pricingViewModel.purches.value!![0])
                    Log.d(TAG_BILLING, "purssss==== ${pricingViewModel.purches.value}")
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

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scaleAnim)
                        .offset(x = offSetAnim)
                        .rotate(rotate)
                        .clip(RoundedCornerShape(clipDp))
                ) {
                    if (pricingViewModel.isSubscribe.value)
                        Toast.makeText(context, "subbbbbbb", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(context, "noooo", Toast.LENGTH_LONG).show()
                    pricingViewModel.handlePurchase()
                    //viewModel.getProducts(context)
                    NavigationHost(navController = navController, viewModel)
                }

                //Splash(navController,viewModel)

                if (viewModel.openPremium)
                    Premium(close = {
                        viewModel.openPremium = false
                    },
                        purchase = {

                            pricingViewModel.makePurchase((context as Activity))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    myphoto: () -> Unit,
    drawer: () -> Unit,
    open: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier.clickable {
                drawer()
            })
        },
        navigationIcon = {
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
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
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(27.dp)
                    .padding(2.dp)
                    .clickable {
                        open()
                    }
            )

            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(id = R.drawable.photos),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(27.dp)
                    .padding(4.dp)
                    .clickable {
                        myphoto()
                    }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}


