package com.wishes.jetpackcompose.admob

import android.app.Activity
import com.compose.androidtoanime.Utils.AppUtils.Companion.applovinClass
import com.compose.androidtoanime.data.AdProvider.Companion.InterFAN
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener



class Facebook {


    companion object {

        lateinit var interstitialAd: InterstitialAd

        fun showInterstitial(activity: Activity) {
            if (!this::interstitialAd.isInitialized) {
                loadInterstitialFAN(activity)
                return
            }
            if (!interstitialAd.isAdLoaded) {
                loadInterstitialFAN(activity)
                return
            }
            if (countShow % InterFAN.show_count!! != 0) {
                return
            }
            interstitialAd.show()

        }

        fun loadInterstitialFAN(activity: Activity) {
            interstitialAd = InterstitialAd(
                activity,
                InterFAN.ad_id
            )
            val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
                override fun onError(p0: Ad?, p1: AdError?) {

                    applovinClass.createInterstitialAd(activity)
                    InterFAN.ad_status = false
                }

                override fun onAdLoaded(inter: Ad?) {

                }

                override fun onAdClicked(p0: Ad?) {

                }

                override fun onLoggingImpression(p0: Ad?) {

                }

                override fun onInterstitialDisplayed(p0: Ad?) {

                }

                override fun onInterstitialDismissed(p0: Ad?) {

                }
            }
            interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build()
            )

        }

    }


}