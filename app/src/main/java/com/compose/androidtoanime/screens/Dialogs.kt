package com.compose.androidtoanime.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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


@Composable
fun Premium(close: () -> Unit,purchase:()->Unit) {
    Dialog(
        onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(top = 50.dp, start = 6.dp, end = 6.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = "Subscription",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Icon(Icons.Default.Close, contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(10.dp)
                            .clickable {
                                close()
                            }
                    )
                }

                itemSub("Unlimited")
                itemSub("Remove Ads")
                itemSub("High Quality")
                itemSub("Speed Converting")

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme.primary,

                            ),
                    ),
                    onClick = {
                        purchase()
                    }) {
                    Text(text = "Buy Now 4,99$/mo")
                }
            }
            Image(
                painter = painterResource(id = R.drawable.diamond),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(16.dp)
            )
        }
    }


}

@Composable
fun itemSub(text: String) {
    Row(modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
        Image(
            painter = painterResource(id = R.drawable.check),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}