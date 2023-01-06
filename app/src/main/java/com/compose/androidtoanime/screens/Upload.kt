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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils.Companion.queryImage
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.viewmodels.ViewModel
import java.io.File


fun checkPermission(context: Context) {

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Upload(navController: NavHostController, viewModel: ViewModel) {

    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var image: File
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var pathImage: String ?=null

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


    if (imageUri != null) {
        pathImage=queryImage(context = context, uri = imageUri!!)
        //Log.d(TAG_D, )

    }


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val imagePicker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri ->
                    if (uri != null)
                        imageUri = uri
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )

//                Box(
//                    Modifier
//                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                        .border(1.dp,MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(6.dp))
//                        .size(300.dp)
//                        .pointerInput(Unit) {
//                            detectDragGestures { change, dragAmount ->
//                                change.consumeAllChanges()
//                                offsetX += dragAmount.x
//                                offsetY += dragAmount.y
//                            }
//                        }
//                )


                when (viewModel.readyImage) {
                    is NetworkResults.Success -> {
                        val data = viewModel.readyImage!!.data
                        val url = BuildConfig.api + data?.folder + "crop" + data?.filename
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            //contentScale = ContentScale.Crop
                        )
                    }
                    is NetworkResults.Error -> {
                        Toast.makeText(context, "err", Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResults.Loading -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {

                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) -> {
                            // Some works that require permission
                            Log.d("ExampleScreen", "Code requires permission")
                        }
                        else -> {
                            // Asking for permission
                            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    }
                    imagePicker.launch("image/*")

                    // Log.d(TAG_D,)
                    //compressImage(imageUri?.path!!)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.upload),
                        contentDescription = null,
                        modifier = Modifier.size(27.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Upload")
                }
                Button(onClick = {
                    //viewModel.test()
                    //val res: Resources = context.resources
                    //val drawableId: Int = R.drawable.index  // the ID of the drawable resource
                    //val drawable: Drawable = res.getDrawable(drawableId, null)
                    //val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
                    //val uri: Uri = getBitmapUri(context, bitmap)
                    //val contentResolver: ContentResolver = context.contentResolver
                    //val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                    //val bitmap = BitmapFactory.decodeStream(inputStream)
                    if (pathImage != null) {
                        //val uri: Uri = getBitmapUri(context, bitmap)
                        viewModel.upload(pathImage)
                    }

                    Log.d("tbCats", imageUri.toString())
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.image), contentDescription = null,
                        modifier = Modifier.size(27.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Camera")
                }
            }

        }
    }

}


