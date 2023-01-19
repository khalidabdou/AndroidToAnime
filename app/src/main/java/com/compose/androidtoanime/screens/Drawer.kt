package com.compose.androidtoanime.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.compose.androidtoanime.R
import com.compose.androidtoanime.Utils.AppUtils


@Composable
fun MyNavigationDrawer(onClick: () -> Unit) {
    val context = LocalContext.current
    Image(
        painter = painterResource(id = R.drawable.wallpaper),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(0.9f),
                        MaterialTheme.colorScheme.background.copy(0.7f),
                    )
                )
            ),
    ) {


        Column() {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = context.getString(R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            ItemDrawer(stringResource(R.string.invite), painterResource(id = R.drawable.user)) {
                AppUtils.share(context)
                //showInterstitialAfterClick(context,)
            }
            ItemDrawer(stringResource(R.string.rate), painterResource(id = R.drawable.star)) {
                AppUtils.rateApp(context)
                //showInterstitialAfterClick(context)
            }

            ItemDrawer(stringResource(R.string.feed), painterResource(id = R.drawable.feed)) {
                AppUtils.sendEmail(context)
                //showInterstitialAfterClick(context)
            }
            ItemDrawer(stringResource(R.string.privacy), painterResource(id = R.drawable.policy)) {
                AppUtils.openStore("https://www.animemagic.fun/privacy", context)
                //showInterstitialAfterClick(context)
            }
            ItemDrawer(stringResource(R.string.site), painterResource(id = R.drawable.site)) {
                AppUtils.openStore(context.getString(R.string.site_url), context)
                //showInterstitialAfterClick(context)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Icon(
                    Icons.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.background,
                    contentDescription = "",
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .clickable {
                            onClick()
                        }
                )
            }
        }

    }
}

@Composable
fun ItemDrawer(text: String, icon: Painter, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        Icon(
            icon, contentDescription = "", tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }

}