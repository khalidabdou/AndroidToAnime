package com.compose.androidtoanime.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils.Companion.saveImage
import com.compose.androidtoanime.Utils.AppUtils.Companion.sharePalette
import com.compose.androidtoanime.Utils.AppUtils.Companion.toBitmap
import com.compose.androidtoanime.viewmodels.ViewModel
import java.util.concurrent.Executors

@Composable
fun Share(viewModel: ViewModel) {

    val data = viewModel.readyImage!!.data
    val url = BuildConfig.api + data?.folder + "crop" + data?.filename
    val context= LocalContext.current

    val myExecutor = Executors.newSingleThreadExecutor()
    val myHandler = Handler(Looper.getMainLooper())
    Column() {

        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            //contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            MyButton(
                text = "Share",
                painter = painterResource(id = R.drawable.ic_share)
            ) {
                myExecutor.execute {
                    val mImage = toBitmap(context,url)
                    myHandler.post {
                        //mImageView.setImageBitmap(mImage)
                        if(mImage!=null){
                            sharePalette(context,mImage)
                        }
                    }
                }

            }
            MyButton(
                text = "Download",
                painter = painterResource(id = R.drawable.ic_download)
            ) {
                myExecutor.execute {
                    val mImage = toBitmap(context,url)
                    myHandler.post {
                        //mImageView.setImageBitmap(mImage)
                        if(mImage!=null){
                            saveImage(context,mImage)
                        }
                    }
                }
                //saveImage(context = context, toBitmap(context,url))
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