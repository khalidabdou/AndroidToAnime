package com.compose.androidtoanime.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils.Companion.saveImage
import com.compose.androidtoanime.Utils.AppUtils.Companion.sharePalette
import com.compose.androidtoanime.Utils.AppUtils.Companion.toBitmap
import com.compose.androidtoanime.viewmodels.MainViewModel
import com.compose.androidtoanime.viewmodels.PricingViewModel
import com.wishes.jetpackcompose.admob.showInterstitialAfterClick
import java.util.concurrent.Executors

@Composable
fun Share(viewModel: MainViewModel, pricingViewModel: PricingViewModel) {

    val context = LocalContext.current
    val myExecutor = Executors.newSingleThreadExecutor()
    val myHandler = Handler(Looper.getMainLooper())

    Column() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            //LoadingAnimation1()
            SubcomposeAsyncImage(
                model = viewModel.getUrl(),
                loading = {
                    LoadingAnimation1()
                },
                contentDescription = stringResource(R.string.description)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MyButton(
                text = stringResource(R.string.download),
                painter = painterResource(id = R.drawable.download)
            ) {
                showInterstitialAfterClick(context, pricingViewModel.isSubscribe.value)
                myExecutor.execute {
                    val mImage = toBitmap(context, viewModel.getUrl())
                    myHandler.post {
                        //mImageView.setImageBitmap(mImage)
                        if (mImage != null) {
                            saveImage(context, mImage)
                        }
                    }
                }
                //saveImage(context = context, toBitmap(context,url))
            }
            MyButton(
                text = stringResource(R.string.share),
                painter = painterResource(id = R.drawable.share)
            ) {
                showInterstitialAfterClick(context, pricingViewModel.isSubscribe.value)
                myExecutor.execute {
                    val mImage = toBitmap(context, viewModel.getUrl())
                    myHandler.post {
                        //mImageView.setImageBitmap(mImage)
                        if (mImage != null) {
                            sharePalette(context, mImage, null)
                        }
                    }
                }

            }
            MyButton(
                text = null,
                painter = painterResource(id = R.drawable.whatsapp)
            ) {
                showInterstitialAfterClick(context, pricingViewModel.isSubscribe.value)
                myExecutor.execute {
                    val mImage = toBitmap(context, viewModel.getUrl())
                    myHandler.post {
                        //mImageView.setImageBitmap(mImage)
                        if (mImage != null) {
                            sharePalette(context, mImage, "")
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun MyButton(text: String?, painter: Painter, onClick: () -> Unit) {
    Button(onClick = {
        onClick()
    }) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(27.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        if (text != null)
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
    }
}