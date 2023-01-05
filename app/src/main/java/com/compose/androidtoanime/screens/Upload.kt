package com.compose.androidtoanime.screens

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils.Companion.getBitmapUri
import com.compose.androidtoanime.viewmodels.ViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Upload(navController: NavHostController, viewModel: ViewModel) {


    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
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
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    imagePicker.launch("image/*")
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
                    val res: Resources = context.resources
                    val drawableId: Int = R.drawable.index  // the ID of the drawable resource
                    val drawable: Drawable = res.getDrawable(drawableId, null)
                    val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
                    val uri: Uri = getBitmapUri(context, bitmap)
                    //if (imageUri!=null)
                    viewModel.test(uri,context)
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