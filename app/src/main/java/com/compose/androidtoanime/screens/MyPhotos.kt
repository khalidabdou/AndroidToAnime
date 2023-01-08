package com.compose.androidtoanime.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.viewmodels.ViewModel

@Composable
fun MyPhotos(viewModel: ViewModel) {

    val data = viewModel.readyImage!!.data
    val url = BuildConfig.api + data?.folder + "crop" + data?.filename
    val context = LocalContext.current

    LaunchedEffect(viewModel.getPhotos()){
        viewModel.getPhotos()
    }

    LazyColumn() {
        items(viewModel.myPhotos.size) {
            Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))){
                AsyncImage(model = "",
                    contentDescription = null)
            }
        }
    }
}
