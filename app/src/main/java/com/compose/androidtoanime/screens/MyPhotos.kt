package com.compose.androidtoanime.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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

    //val data = viewModel.readyImage!!.data
    //val url = BuildConfig.api + data?.folder + "crop" + data?.filename
    val context = LocalContext.current

    LaunchedEffect(viewModel.getPhotos()){
        viewModel.getPhotos()
    }

    LazyColumn() {
        items(viewModel.myPhotos.size) {

            val url = BuildConfig.api + viewModel.myPhotos[it].folder + "crop" + viewModel.myPhotos[it].filename
            Box(modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)){
                AsyncImage(model = url,
                    contentDescription = null)
            }
        }
    }
}
