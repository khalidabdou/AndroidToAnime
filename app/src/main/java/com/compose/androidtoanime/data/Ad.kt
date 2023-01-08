package com.compose.androidtoanime.data

import com.google.gson.annotations.SerializedName

data class Ad(
    var id: Int,
    var ad_id: String,
    var type: String,
    var ad_status: Boolean,
    var show_count: Int?,

    )


data class Ads(
    @SerializedName(value = "admobe", alternate = ["ads"])
    val ads: List<Ad>,

)

class AdProvider {

    companion object {
        var Banner: Ad = Ad(
            0,
            "",
            "banner",
            false,
            null,
        )
        var Inter: Ad = Ad(
            0,
            "",
            "inter",
            true,
            3,
        )
        var OpenAd: Ad = Ad(
            0,
            "",
            "open",
            false,
            null,
        )
        var Rewarded: Ad = Ad(
            0,
            "",
            "rewarded",
            false,
            null,
        )


        var BannerFAN: Ad = Ad(
            0,
            "",
            "banner_fan",
            false,
            null,
        )
        var InterFAN: Ad = Ad(
            0,
            "",
            "inter_fan",
            false,
            10,
        )
        var BannerApplovin: Ad = Ad(
            0,
            "",
            "banner_applovin",
            false,
            null,
        )
        var InterApplovin: Ad = Ad(
            0,
            "",
            "inter_Applovin",
            false,
            10,
        )
        //banner_applovin
    }
}