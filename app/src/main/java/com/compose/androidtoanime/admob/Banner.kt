package com.ringtones.compose.feature.admob

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.compose.androidtoanime.data.AdProvider.Companion.Banner
import com.compose.androidtoanime.data.AdProvider.Companion.BannerApplovin
import com.compose.androidtoanime.data.AdProvider.Companion.BannerFAN
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.google.android.gms.ads.*


@Composable
fun AdvertViewAdmob(modifier: Modifier = Modifier) {
    val isInEditMode = LocalInspectionMode.current
    var state by remember { mutableStateOf(true) }
    var stateFb by remember { mutableStateOf(true) }
    var stateApplovin by remember { mutableStateOf(true) }
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxSize()
                .background(Red)
                .padding(horizontal = 2.dp, vertical = 2.dp),
            textAlign = TextAlign.Center,
            color = White,
            text = "",
        )
    } else if (state && Banner.ad_status) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(AdSize.BANNER.height.dp + 4.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 4.dp, bottom = 4.dp),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = Banner.ad_id
                    loadAd(AdRequest.Builder().build())
                    adListener = object : AdListener() {
                        override fun onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        override fun onAdClosed() {
                            // Code to be executed when the user is about to return
                            // to the app after tapping on an ad.
                        }

                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            state = false
                        }

                        override fun onAdImpression() {
                            // Code to be executed when an impression is recorded
                            // for an ad.
                        }

                        override fun onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                        }

                        override fun onAdOpened() {
                            // Code to be executed when an ad opens an overlay that
                            // covers the screen.
                        }
                    }
                }
            }
        )
    } else if (BannerFAN.ad_status && stateFb) {
        val adListenerfb: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
            override fun onError(p0: Ad?, p1: AdError?) {
                stateFb = false

            }

            override fun onAdLoaded(p0: Ad?) {

            }

            override fun onAdClicked(p0: Ad?) {

            }

            override fun onLoggingImpression(p0: Ad?) {

            }
        }

        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(AdSize.BANNER.height.dp + 4.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 4.dp, bottom = 4.dp),
            factory = { context ->
                com.facebook.ads.AdView(
                    context,
                    BannerFAN.ad_id,
                    com.facebook.ads.AdSize.BANNER_HEIGHT_50
                ).apply {
                    loadAd(buildLoadAdConfig().withAdListener(adListenerfb).build())
                }
            }
        )
    } else if (BannerApplovin.ad_status) {
        val adListenerMax: MaxAdViewAdListener = object : MaxAdViewAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdDisplayed(ad: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdHidden(ad: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdClicked(ad: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                stateApplovin = false
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                TODO("Not yet implemented")
            }

            override fun onAdExpanded(ad: MaxAd?) {
                TODO("Not yet implemented")
            }

            override fun onAdCollapsed(ad: MaxAd?) {
                TODO("Not yet implemented")
            }

        }
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(AdSize.BANNER.height.dp + 4.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 4.dp, bottom = 4.dp),
            factory = { context ->
                MaxAdView(BannerApplovin.ad_id, context).apply {
                    loadAd()
                    setListener(adListenerMax)
                }

            })
    }
}


@Composable
fun AdvertViewFAN(modifier: Modifier = Modifier) {
    Log.d("FAN", BannerFAN.ad_id)
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(AdSize.BANNER.height.dp + 4.dp)
            .background(MaterialTheme.colorScheme.surface),
        factory = { context ->
            com.facebook.ads.AdView(
                context,
                BannerFAN.ad_id,
                com.facebook.ads.AdSize.BANNER_HEIGHT_50
            ).apply {
                loadAd()
            }
        }
    )

}

