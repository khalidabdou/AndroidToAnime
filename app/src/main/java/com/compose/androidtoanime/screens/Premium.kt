package com.compose.androidtoanime.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.compose.androidtoanime.R
import com.compose.androidtoanime.viewmodels.PricingViewModel

@Composable
fun Premium(navHostController: NavHostController, pricing: PricingViewModel) {

    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background.copy(0f),
                                MaterialTheme.colorScheme.background,
                            ),
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background.copy(0.5f))
                    .align(Alignment.TopEnd)
                    .clickable {
                        navHostController.popBackStack()
                    },
            )
        }
        Column(modifier = Modifier.padding(start = 60.dp, top = 20.dp)) {
            itemSub("Unlimited")
            itemSub("Remove Ads")
            itemSub("Remove watermark")
            itemSub("Photos High Quality")
            itemSub("Speed Converting")
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Subscription",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()

        )
        Text(
            text = "Subscription can be canceled any time",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        itemButton("9.99$ / WEEk", "+ 2 days free trail") {
            pricing.makePurchase(
                0,
                (context as Activity)
            )
        }
        itemButton("19.99$ / MONTH", "+ 7 days free trail") {
            pricing.makePurchase(
                1,
                (context as Activity)
            )
        }

    }
}

@Composable
fun itemButton(subText: String, descText: String, onClick: () -> Unit) {
    Box() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.onBackground)
                .clickable {
                    onClick()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = subText,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.background

            )
            Text(
                text = descText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.background
            )
        }

//        Text(
//            text = "-20%",
//            style = MaterialTheme.typography.labelMedium,
//            textAlign = TextAlign.Center,
//            color = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.align(Alignment.BottomEnd).clip()
//        )
    }

}