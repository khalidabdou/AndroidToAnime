package com.compose.androidtoanime.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils.Companion.bitmap
import com.compose.androidtoanime.Utils.AppUtils.Companion.compressImage
import com.compose.androidtoanime.Utils.FileUtil
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.viewmodels.ViewModel


fun checkPermission(context: Context) {

}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Upload(navController: NavHostController, viewModel: ViewModel) {

    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }


    var pathImage: String? = null

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted: Do something
            Log.d("ExampleScreen", "PERMISSION GRANTED")

        } else {
            // Permission Denied: Do something
            Log.d("ExampleScreen", "PERMISSION DENIED")
        }
    }


    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = -100F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    if (imageUri != null) {
        pathImage = FileUtil(context).getPath(imageUri!!)
        bitmap = pathImage?.let { compressImage(it) }!!
        //pathImage = queryImage(context = context, uri = getUri)
        //Log.d(TAG_D, )
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                }
                is NetworkResults.Error -> {
                    AsyncImage(
                        model = painterResource(id = R.drawable.delivery),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                    LoadingAnimation1()
                    Toast.makeText(context, "Please try later", Toast.LENGTH_SHORT).show()

                }
                is NetworkResults.Loading -> {
                    AsyncImage(
                        model = pathImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                    LoadingAnimation1()
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is NetworkResults.NotYet -> {
                    Icon(
                        painter = painterResource(id = R.drawable.gallery),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .offset(y = angle.dp).weight(1f),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (imageUri == null)
                            Button(onClick = {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) -> {
                                        // Some works that require permission
                                        Log.d("ExampleScreen", "Code requires permission")
                                    }
                                    else -> {
                                        // Asking for permission
                                        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        //launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    }
                                }
                                imagePicker.launch("image/*")

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = null,
                                    modifier = Modifier.size(27.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Upload",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        if (imageUri != null) {
                            Button(onClick = {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    ) -> {
                                        // Some works that require permission
                                        Log.d("ExampleScreen", "Code requires permission")
                                    }
                                    else -> {
                                        // Asking for permission
                                        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        //launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    }
                                }
                                imagePicker.launch("image/*")

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.upload),
                                    contentDescription = null,
                                    modifier = Modifier.size(27.dp)
                                )
                            }
                            Button(modifier = Modifier, onClick = {
                                if (pathImage != null) {
                                    //val uri: Uri = getBitmapUri(context, bitmap)
                                    viewModel.upload(pathImage)
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.convert),
                                    contentDescription = null,
                                    modifier = Modifier.size(27.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Convert To Anime",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun TopBar() {
    SmallTopAppBar(
        title = {
            Text(text = "To Anime")
        },
        navigationIcon = {},
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.premium),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(30.dp)
                    .padding(2.dp)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun notYet() {

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
                width = 4.dp,
                color = circleColor.copy(alpha = 1 - circleScaleAnimate.value),
                shape = CircleShape
            )
    ) {

    }
}

