package com.compose.androidtoanime.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.androidtoanime.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            text = stringResource(R.string.wait_converting),
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }
    Toast.makeText(context, stringResource(R.string.loading), Toast.LENGTH_LONG).show()
}


var index = 0


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
fun NotYet(
    context: Context,
    imageUri: Uri?,
    isConverting: MutableState<Boolean>,
    onSelect: () -> Unit,
    convert: () -> Unit
) {
    val TAG = "TAG_NOT_YET"
    val textArray = context.resources.getStringArray(R.array.messages)
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = -100F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Log.d(TAG, "not yet")
    val convertingText = remember { mutableStateOf(context.getString(R.string.ready)) }
    val progress = remember { mutableStateOf(0) }


    val scaleAnimY by animateFloatAsState(
        targetValue = if (imageUri != null) 0f else 200f,
        tween(1500)
    )
    val fraction by animateFloatAsState(
        targetValue = if (imageUri == null) 0.5f else 1f,
        tween(1000)
    )

    val alphaText by animateFloatAsState(
        targetValue = if (imageUri == null) 1f else 0f,
        tween(1000)
    )

    val offsetText by animateDpAsState(
        targetValue = if (imageUri == null) 0.dp else 400.dp,
        tween(1000)
    )

    val alphaReady by animateFloatAsState(
        targetValue = if (imageUri != null) 1f else 0f,
        tween(2000)
    )

    val offsetReady by animateDpAsState(
        targetValue = if (imageUri != null) 0.dp else 400.dp,
        tween(2000)
    )


    val offsetButton by animateDpAsState(
        targetValue = if (isConverting.value) 100.dp else 0.dp,
        tween(3000)
    )

    LaunchedEffect(key1 = isConverting.value) {
        CoroutineScope(Dispatchers.IO).launch {
            while (isConverting.value) {
                convertingText.value = textArray[index++ % textArray.size]
                if (progress.value < 95)
                    progress.value += (2..5).random()
                delay(1000)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        val color = MaterialTheme.colorScheme.background
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction)
                .align(Alignment.BottomCenter)
        ) {
            //drawImage(getBitmapFromImage(context).asImageBitmap(),)
            val path = Path()
            path.moveTo(0f, 0f)
            path.lineTo(size.width, scaleAnimY)
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
                .alpha(alphaText)
                .offset(y = offsetText)
        ) {
            Text(
                text = stringResource(R.string.Heading),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.heading_description),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            if (imageUri == null) {
                Box(modifier = Modifier.weight(1f))
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .alpha(alphaReady)
                        .offset(y = offsetReady),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.FillWidth
                        )

                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = convertingText.value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    valueProgress(progress.value)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (imageUri == null)
                    myButton(
                        modifier = Modifier,
                        text = stringResource(R.string.upload),
                        icon = R.drawable.gallery
                    ) {
                        onSelect()
                    }
                else {
                    myButton(
                        modifier = Modifier.offset(y = offsetButton),
                        stringResource(R.string.convert),
                        R.drawable.convert
                    ) {

                    convert()

                    }
                }
            }
        }
    }
}

@Composable
fun myButton(modifier: Modifier, text: String?, icon: Int, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(27.dp)
        )
        if (text != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun valueProgress(count: Int) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically { height -> height } + fadeIn() with
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically { height -> -height } + fadeIn() with
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }
    ) { targetCount ->
        Text(text = "$targetCount%")
    }

}