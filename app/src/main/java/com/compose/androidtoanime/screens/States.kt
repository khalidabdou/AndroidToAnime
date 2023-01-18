package com.compose.androidtoanime.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.androidtoanime.R

@Composable
fun converting(pathImage: String, context: Context) {
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
        Text(
            text = "Please wait for converting ...",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }
    Toast.makeText(context, "Loading ...", Toast.LENGTH_LONG).show()
}

private fun getBitmapFromImage(context: Context): Bitmap {

    val option = BitmapFactory.Options()
    option.inPreferredConfig = Bitmap.Config.ARGB_8888
    val bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.wallpaper,
        option
    ).asImageBitmap()
    return bitmap.asAndroidBitmap()
}

@Composable
fun notYet(imageUri: Uri?, pathImage: String?, onSelect: () -> Unit, convert: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = -100F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        val context = LocalContext.current

//        Canvas(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.8f)
//                .align(Alignment.BottomCenter)
//        ) {
//
//            val path = Path()
//            path.moveTo(0f, 0f)
//            path.lineTo(size.width, 200f)
//            path.lineTo(size.width, size.height)
//            path.lineTo(0f, size.height)
//            drawPath(
//                path = path,
//                brush = SolidColor(Color.LightGray)
//            )
//
//        }
        val color = MaterialTheme.colorScheme.background
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
        ) {

            //drawImage(getBitmapFromImage(context).asImageBitmap(),)
            val path = Path()
            path.moveTo(0f, 0f)
            path.lineTo(size.width, 200f)
            path.lineTo(size.width, size.height)
            path.lineTo(0f, size.height)
            drawPath(
                path = path,
                brush = SolidColor(color)
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(top = 100.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Anime Magic", style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Convert Your favorite photo to Anime,Your photo must be clear and contain a human face, illegal photos are not accepted",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.background(
//                Brush.linearGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.background,
//                        MaterialTheme.colorScheme.background.copy(0.9f),
//                        MaterialTheme.colorScheme.primary.copy(0.6f),
//                        MaterialTheme.colorScheme.primary.copy(0.7f),
//                    )
//                )
//            ),
        ) {

            if (imageUri == null)
                Icon(
                    painter = painterResource(id = R.drawable.gallery),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .size(50.dp)
                        .offset(y = offset.dp)
                        .weight(1f)

                ) else
                AsyncImage(
                    model = pathImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (imageUri == null)
                    Button(
                        onClick = {
                            onSelect()
                        }, modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onBackground,
                            contentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.gallery),
                            contentDescription = null,
                            modifier = Modifier.size(27.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.upload),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                if (imageUri != null) {
                    OutlinedButton(onClick = {
                        onSelect()


                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.gallery),
                            contentDescription = null,
                            modifier = Modifier.size(27.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                    Button(onClick = {
                        convert()
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