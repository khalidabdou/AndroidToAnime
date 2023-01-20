package com.ringtones.compose.feature.admob

import android.content.Context
import android.util.Log
import com.compose.androidtoanime.Utils.findActivity
import com.compose.androidtoanime.data.AdProvider.Companion.Rewarded


import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

private var mRewardedAd: RewardedAd? = null

fun loadRewarded(context: Context) {

    if (!Rewarded.ad_status)
        return

    var adRequest = AdRequest.Builder().build()
    RewardedAd.load(
        context,
        Rewarded.ad_id,
        adRequest,
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                //Log.d(TAG, adError?.toString())
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                //Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })
}

fun showRewarded(context: Context) {

    if (mRewardedAd != null) {
        val activity = context.findActivity()
        mRewardedAd?.show(activity!!, OnUserEarnedRewardListener {
            fun onUserEarnedReward(rewardItem: RewardItem) {
                var rewardAmount = rewardItem.amount
                var rewardType = rewardItem.type
                //Log.d(TAG, "User earned the reward.")
            }
        })
        mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d("MainActivity", "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d("MainActivity", "Ad dismissed fullscreen content.")
                mRewardedAd = null
                loadRewarded(context)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                Log.e("MainActivity", "Ad failed to show fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d("MainActivity", "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d("MainActivity", "Ad showed fullscreen content.")
            }
        }
    } else {
        loadRewarded(context)
        //Log.d(TAG, "The rewarded ad wasn't ready yet.")
    }
}