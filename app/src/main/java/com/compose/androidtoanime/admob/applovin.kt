package com.wishes.jetpackcompose.admob

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.compose.androidtoanime.data.AdProvider.Companion.InterApplovin
import java.util.concurrent.TimeUnit

class applovin : MaxAdListener {

    private lateinit var interstitialAd: MaxInterstitialAd
    private var retryAttempt = 0.0

    fun createInterstitialAd(context: Context) {
        if (!this::interstitialAd.isInitialized) {
            interstitialAd = MaxInterstitialAd(InterApplovin.ad_id, context as Activity)
            interstitialAd.setListener(this)
            // Load the first ad
            interstitialAd.loadAd()
        }
        if (!interstitialAd.isReady)
            interstitialAd.loadAd()

    }

    // MAX Ad Listener
    override fun onAdLoaded(maxAd: MaxAd) {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        Log.d("applovinad", "onAdLoaded")
        retryAttempt = 0.0
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        Log.d("applovinad", "onAdLoadFailed")
        retryAttempt++
        val delayMillis =
            TimeUnit.SECONDS.toMillis(Math.pow(2.0, Math.min(6.0, retryAttempt)).toLong())

        Handler().postDelayed({ interstitialAd.loadAd() }, delayMillis)
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd.loadAd()
    }

    override fun onAdDisplayed(maxAd: MaxAd) {}

    override fun onAdClicked(maxAd: MaxAd) {}

    override fun onAdHidden(maxAd: MaxAd) {
        // Interstitial ad is hidden. Pre-load the next ad
        interstitialAd.loadAd()
    }

    fun show(context: Context) {
        if (interstitialAd.isReady) {
            Log.d("applovinad", "show2 " + countShow.toString())

            if (countShow % InterApplovin.show_count!! == 0)
                interstitialAd.showAd()
        } else {
            createInterstitialAd(context)
        }
    }

}