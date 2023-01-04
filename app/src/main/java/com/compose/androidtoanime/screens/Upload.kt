package com.compose.androidtoanime.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.viewmodels.ViewModel


@Composable
fun Upload(navController: NavHostController, viewModel: ViewModel) {


    val context = LocalContext.current
    var imageUri: Any? = remember {
        mutableStateOf(R.drawable.ic_launcher_foreground)
    }
//    val photoPickers = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicturePreview,
//        onResult = { imageUri = it }
//    )

    Surface {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {

            }
            Button(onClick = { }) {
                Icon(imageVector = Icons.Default.Face, contentDescription = null)
                Spacer(modifier = Modifier.width(0.dp))
                Text(text = "Upload picture")
            }
            Button(onClick = { }) {
                Icon(imageVector = Icons.Default.Face, contentDescription = null)
                Spacer(modifier = Modifier.width(0.dp))
                Text(text = "take picture")
            }
        }
    }

}