package com.compose.androidtoanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.compose.androidtoanime.Utils.AppUtils.Companion.ENABLE_PREMIUM
import com.compose.androidtoanime.screens.DialogExit
import com.compose.androidtoanime.screens.HowToUse
import com.compose.androidtoanime.screens.MyNavigationDrawer
import com.compose.androidtoanime.ui.theme.AndroidToAnimeTheme
import com.compose.androidtoanime.viewmodels.ViewModel
import com.wishes.jetpackcompose.runtime.NavigationHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidToAnimeTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: ViewModel = hiltViewModel()
                val navController = rememberNavController()
                val context= LocalContext.current
                //create animations
                //var navigateClick by remember { mutableStateOf(false) }

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

                    NavigationHost(navController = navController, viewModel)
                }

                //Splash(navController,viewModel)

                if (viewModel.openPremium)
                    Premium() {
                        viewModel.openPremium = false
                    }

                if (viewModel.openExit)
                    DialogExit(context){
                        viewModel.openExit=false
                    }

                if (viewModel.openHow)
                    HowToUse(context){
                        viewModel.openHow=false
                    }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(myphoto: () -> Unit,how:()->Unit, drawer: () -> Unit, share: () -> Unit, open: () -> Unit) {
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
            if (ENABLE_PREMIUM)
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
            Icon(
                painter = painterResource(id = R.drawable.how),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(27.dp)
                    .padding(2.dp)
                    .clickable {
                        how()
                    }
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(27.dp)
                    .padding(2.dp)
                    .clickable {
                        share()
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

@Composable
fun Premium(close: () -> Unit) {
    Dialog(
        onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
    ) {

        Box(contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(top = 50.dp, start = 6.dp, end = 6.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = "Subscription",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Icon(Icons.Default.Close, contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(10.dp)
                            .clickable {
                                close()
                            }
                    )
                }

                itemSub("Unlimited")
                itemSub("Remove Ads")
                itemSub("High Quality")
                itemSub("Speed Converting")

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme.primary,

                            ),
                    ),
                    onClick = {}) {
                    Text(text = "Buy Now 4,99$/mo")
                }
            }
            Image(
                painter = painterResource(id = R.drawable.diamond),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(16.dp)
            )
        }
    }


}

@Composable
fun itemSub(text: String) {
    Row(modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
        Image(
            painter = painterResource(id = R.drawable.check),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
