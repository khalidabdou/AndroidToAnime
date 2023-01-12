package com.compose.androidtoanime.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils

@Composable
fun DialogExit(context: Context, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            //showAlertDialog = false
            onDismiss()
        },
        title = {
            Row() {
                Text(
                    context.getString(R.string.title),
                    modifier = Modifier.weight(1f)
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = "cancel",
                    tint = MaterialTheme.colorScheme.primary.copy(0.6f),
                    modifier = Modifier.clickable {
                        onDismiss()
                    }
                )
            }
        },
        text = {
            Column() {
                Text(text = context.getString(R.string.dailog_desc))
                Spacer(modifier = Modifier.height(5.dp))
            }


        },
        confirmButton = {
            Button(onClick = {
                AppUtils.rateApp(context)
            }) {
                Text(text = context.getString(R.string.rate_title))
            }

        },
        dismissButton = {
            Button(onClick = {
                (context as Activity).finish()
            }) {
                Text(text =  context.getString(R.string.exit))
            }
        },
    )
}


@Composable
fun HowToUse(context: Context, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            //showAlertDialog = false
            onDismiss()
        },
        title = {
            Row() {
                Text(
                    context.getString(R.string.How_to),
                    modifier = Modifier.weight(1f)
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = "cancel",
                    tint = MaterialTheme.colorScheme.primary.copy(0.6f),
                    modifier = Modifier.clickable {
                        onDismiss()
                    }
                )
            }
        },
        text = {
            Column() {
                Text(text = context.getString(R.string.how_to_desc))
                Spacer(modifier = Modifier.height(5.dp))
            }


        },
        confirmButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = context.getString(R.string.dismiss))
            }

        },
    )
}