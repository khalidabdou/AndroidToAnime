package com.compose.androidtoanime.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils
import com.compose.androidtoanime.Utils.AppUtils.Companion.bitmap
import com.compose.androidtoanime.Utils.AppUtils.Companion.compressImage
import com.compose.androidtoanime.Utils.AppUtils.Companion.hasStoragePermission
import com.compose.androidtoanime.Utils.FileUtil
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.compose.androidtoanime.viewmodels.PricingViewModel
import com.wishes.jetpackcompose.admob.loadInterstitial
import com.wishes.jetpackcompose.runtime.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Upload(
    navController: NavHostController,
    viewModel: MainViewModel,
    pricingViewModel: PricingViewModel
) {

    val context = LocalContext.current
    val isSubscribed = pricingViewModel.isSubscribe.value

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var converting = remember {
        mutableStateOf(false)
    }

    var openPermission by remember {
        if (hasStoragePermission(context))
            mutableStateOf(false)
        else mutableStateOf(true)
    }
    LaunchedEffect(key1 = Unit, block = {
        loadInterstitial(context, pricingViewModel.isSubscribe.value)
        viewModel.getPhotos()
    })
    var pathImage: String? = null

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen", "PERMISSION GRANTED")
            openPermission = false

        } else {
            // Permission Denied: Do something
            openPermission = true
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }


    //scan photo animation
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )

    if (imageUri != null) {
        if (hasStoragePermission(context)) {
            pathImage = FileUtil(context).getPath(imageUri!!)
            bitmap = pathImage?.let { compressImage(it, isSubscribed) }!!
        } else
            Toast.makeText(context, stringResource(R.string.try_later), Toast.LENGTH_SHORT).show()
    }
    BackHandler() {
        //return@BackHandler
        if (viewModel.readyImage is NetworkResults.Loading) {
            Toast.makeText(context, context.getString(R.string.wait), Toast.LENGTH_SHORT).show()
            return@BackHandler
        }
        if (viewModel.readyImage !is NetworkResults.NotYet) {
            //imageUri=null
            viewModel.readyImage = NetworkResults.NotYet()
        } else if (imageUri == null) {
            viewModel.openExit = true
        }

        imageUri = null

    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null)
                imageUri = uri
        }
    )

    when (viewModel.readyImage) {
        is NetworkResults.Success -> {
            Share(viewModel = viewModel, pricingViewModel)
            converting.value = false
        }
        is NetworkResults.Error -> {
            Toast.makeText(context, context.getString(R.string.try_later), Toast.LENGTH_SHORT)
                .show()
            viewModel.readyImage = NetworkResults.NotYet()
            converting.value = false
        }

        else -> {
            converting.value = viewModel.readyImage is NetworkResults.Loading

            NotYet(
                context = context,
                imageUri = imageUri,
                isConverting = converting,
                animeBot = {
                    navController.navigate(NavRoutes.Chat.route)
                },
                onSelect = {
                    if (hasStoragePermission(context)) {
                        imagePicker.launch("image/*")
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.permission),
                            Toast.LENGTH_SHORT
                        ).show()
                        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }) {

                if (pathImage != null) {
                    if (viewModel.myPhotos.size > AppUtils.MAX_PHOTO && !isSubscribed) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.upgrade_message),
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(NavRoutes.Premium.route)
                    } else {
                        viewModel.readyImage = NetworkResults.Loading()
                        viewModel.convert(pathImage)
                    }

                    Log.d(AppUtils.TAG_D, "${viewModel.myPhotos.size}")
                }
            }
        }
    }
}


@Composable
fun LoadingAnimation1(
    circleColor: Color = Color.Magenta,
    animationDelay: Int = 1000
) {

    // circle's scale state
    var circleScale by remember {
        mutableStateOf(0f)
    }

    // animation
    val circleScaleAnimate = animateFloatAsState(
        targetValue = circleScale,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDelay
            )
        )
    )

    // This is called when the app is launched
    LaunchedEffect(Unit) {
        circleScale = 1f
    }

    // animating circle
    Box(
        modifier = Modifier
            .size(size = 64.dp)
            .scale(scale = circleScaleAnimate.value)
            .border(
                width = 10.dp,
                color = circleColor.copy(alpha = 1 - circleScaleAnimate.value),
                shape = CircleShape
            )
    ) {

    }
}


@Composable
fun Permission(onConfirm: () -> Unit) {
    AlertDialog(onDismissRequest = {},
        title = {},
        text = {
            Text(
                text = stringResource(R.string.needed_permission),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text(text = stringResource(R.string.agree))
            }
        }
    )
}






