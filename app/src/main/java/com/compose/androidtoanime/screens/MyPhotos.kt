package com.compose.androidtoanime.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.R
import com.compose.androidtoanime.viewmodels.ViewModel

@Composable
fun MyPhotos(viewModel: ViewModel) {

    //val data = viewModel.readyImage!!.data
    //val url = BuildConfig.api + data?.folder + "crop" + data?.filename
    val context = LocalContext.current

    LaunchedEffect(viewModel.getPhotos()) {
        viewModel.getPhotos()
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp,MaterialTheme.colorScheme.primary)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = null
                    )
                }
            }
        }
}
