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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils.Companion.MAX_PHOTO
import com.compose.androidtoanime.Utils.AppUtils.Companion.bitmap
import com.compose.androidtoanime.Utils.AppUtils.Companion.compressImage
import com.compose.androidtoanime.Utils.AppUtils.Companion.hasStoragePermission
import com.compose.androidtoanime.Utils.FileUtil
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.wishes.jetpackcompose.admob.loadInterstitial

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Upload(navController: NavHostController, viewModel: MainViewModel) {

    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    var openPermission by remember {
        if (hasStoragePermission(context))
            mutableStateOf(false)
        else mutableStateOf(true)
    }

    LaunchedEffect(key1 = true, block = {
        loadInterstitial(context)
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


    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = -100F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

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
            bitmap = pathImage?.let { compressImage(it) }!!
        } else
            Toast.makeText(context, stringResource(R.string.try_later), Toast.LENGTH_SHORT).show()
    }

    BackHandler() {
        if (viewModel.readyImage is NetworkResults.Loading) {
            Toast.makeText(context, context.getString(R.string.wait), Toast.LENGTH_SHORT).show()
            return@BackHandler
        }
        if (viewModel.readyImage !is NetworkResults.NotYet) {
            //imageUri=null
            viewModel.readyImage = NetworkResults.NotYet()
        } else
            viewModel.openExit = true

    }


    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier) {
        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null)
                    imageUri = uri
            }
        )

        when (viewModel.readyImage) {
            is NetworkResults.Success -> {
                Share(viewModel = viewModel)
                //viewModel.readyImage = NetworkResults.NotYet()
                //navController.navigate(NavRoutes.Share.route)
            }
            is NetworkResults.Error -> {
                Toast.makeText(context, context.getString(R.string.try_later), Toast.LENGTH_SHORT).show()
                viewModel.readyImage = NetworkResults.NotYet()
            }
            is NetworkResults.Loading -> {

                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = pathImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                            setToSaturation(
                                0f
                            )
                        }),
                    )
                    LoadingAnimation1()
                    //GifImage()
                }

                //Toast.makeText(context, "Loading ...", Toast.LENGTH_LONG).show()
            }
            is NetworkResults.NotYet -> {
                if (openPermission)
                    Permission {
                        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                if (imageUri == null)
                    Icon(
                        painter = painterResource(id = R.drawable.gallery),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .offset(y = offset.dp)
                            .weight(1f),
                    ) else
                    AsyncImage(
                        model = pathImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (imageUri == null)
                        OutlinedButton(onClick = {
                            imagePicker.launch("image/*")
                            if (!hasStoragePermission(context))
                                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)


                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.upload),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    if (imageUri != null) {
                        OutlinedButton(onClick = {
                            if (hasStoragePermission(context)) {
                                imagePicker.launch("image/*")
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.permission),
                                    Toast.LENGTH_SHORT
                                ).show()
                                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }


                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.gallery),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                        }
                        Button(modifier = Modifier, onClick = {
                            if (pathImage != null) {
                                if (viewModel.myPhotos.size > MAX_PHOTO) {
                                    viewModel.openPremium = true
                                } else
                                    viewModel.upload(pathImage)
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.magic),
                                contentDescription = null,
                                modifier = Modifier.size(27.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.convert),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
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






