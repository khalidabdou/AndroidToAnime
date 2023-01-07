package com.compose.androidtoanime.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.R
import com.compose.androidtoanime.viewmodels.ViewModel

@Composable
fun Share(viewModel: ViewModel) {
    Column() {
        val data = viewModel.readyImage!!.data
        val url = BuildConfig.api + data?.folder + "crop" + data?.filename
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
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

            }
            MyButton(
                text = "Download",
                painter = painterResource(id = R.drawable.ic_download)
            ) {

            }
        }
    }
}

@Composable
fun MyButton(text: String, painter: Painter, onClick: () -> Unit) {
    Button(onClick = {
        onClick()
    }) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(27.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}