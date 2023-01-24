package com.compose.androidtoanime.Utils

import android.util.Log
import com.compose.androidtoanime.data.Ad
import com.compose.androidtoanime.data.AdProvider

class HandleAds(ads: List<Ad>) {

    val ads = ads

    fun setAds() {
        ads.forEach {
            Log.d("FAN", it.ad_id)
            when (it.type) {
                "banner" -> {
                    AdProvider.Banner = it
                    Log.d("ads", AdProvider.Banner.toString())
                }
                "inter" -> {
                    AdProvider.Inter = it
                    Log.d("ads", AdProvider.Inter.toString())
                }
                "open" -> {
                    AdProvider.OpenAd = it
                    Log.d("ads", AdProvider.OpenAd.toString())
                }
                "rewarded" -> {
                    AdProvider.Rewarded = it
                    //Log.d("ads", OpenAd.toString())
                }
                "banner_fan" -> {
                    Log.d("FAN", it.ad_id)
                    AdProvider.BannerFAN = it
                    //Log.d("ads", Banner.toString())
                }
                "inter_fan" -> {
                    AdProvider.InterFAN = it
                    //Log.d("ads", Inter.toString())
                }
                "banner_applovin" -> {
                    Log.d("FAN", it.ad_id)
                    AdProvider.BannerApplovin = it
                    //Log.d("ads", Banner.toString())
                }
                "inter_Applovin" -> {
                    AdProvider.InterApplovin = it
                    //Log.d("ads", Inter.toString())
                }
            }
        }
    }

}