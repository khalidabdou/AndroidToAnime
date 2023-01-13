package com.compose.androidtoanime.screens

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.wishes.jetpackcompose.admob.showInterstitialAfterClick
import java.util.concurrent.Executors

@Composable
fun MyPhotos(viewModel: MainViewModel) {

    //val data = viewModel.readyImage!!.data
    //val url = BuildConfig.api + data?.folder + "crop" + data?.filename
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        Log.d("database","--")
        viewModel.getPhotos()
        showInterstitialAfterClick(context)
    }

    if (viewModel.myPhotos.size <= 0) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.magic),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = stringResource(id = R.string.empty),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }
    } else
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.myPhotos.size) {
                val url =
                    BuildConfig.api + viewModel.myPhotos[it].folder + "crop" + viewModel.myPhotos[it].filename
                MyPhotoItem(url, context) {
                    viewModel.deleteFromRoom(viewModel.myPhotos[it])
                }
            }
        }
}

@Composable
fun MyPhotoItem(url: String, context: Context, onDelete: () -> Unit) {
    val myExecutor = Executors.newSingleThreadExecutor()
    val myHandler = Handler(Looper.getMainLooper())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .background(MaterialTheme.colorScheme.primary)
            .clickable {

            },
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxSize()

                .background(MaterialTheme.colorScheme.background.copy(0.5f)),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                painter = painterResource(id = R.drawable.share),
                contentDescription = null,
                modifier = Modifier
                    .size(27.dp).padding(6.dp)
                    .clickable {
                        myExecutor.execute {
                            val mImage = AppUtils.toBitmap(context, url)
                            myHandler.post {
                                //mImageView.setImageBitmap(mImage)
                                if (mImage != null) {
                                    AppUtils.sharePalette(context, mImage,null)
                                }
                            }
                        }

                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.download),
                contentDescription = null,
                modifier = Modifier
                    .size(27.dp).padding(2.dp)
                    .clickable
                    {
                        showInterstitialAfterClick(context)
                        myExecutor.execute {
                            val mImage = AppUtils.toBitmap(context, url)
                            myHandler.post {
                                //mImageView.setImageBitmap(mImage)
                                if (mImage != null) {
                                    AppUtils.saveImage(context, mImage)
                                }
                            }
                        }
                        //saveImage(context = context, toBitmap(context,url))
                    }

            )
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = null,
                modifier = Modifier
                    .size(27.dp).padding(6.dp)
                    .clickable
                    {
                        showInterstitialAfterClick(context)
                        onDelete()
                    }

            )

        }

    }
}
