package com.compose.androidtoanime.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(0.9f),
                        MaterialTheme.colorScheme.primary.copy(0.6f),
                        MaterialTheme.colorScheme.primary.copy(0.7f),
                    )
                )
            ),
        ) {

            if (imageUri == null)
                Icon(
                    painter = painterResource(id = R.drawable.gallery),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
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
                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (imageUri == null)
                    Button(onClick = {
                        onSelect()
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
                        onSelect()


                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.gallery),
                            contentDescription = null,
                            modifier = Modifier.size(27.dp)
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