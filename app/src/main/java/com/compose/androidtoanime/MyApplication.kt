package com.compose.androidtoanime

import android.app.Application
import com.facebook.ads.AdSettings
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        AdSettings.addTestDevice("e144ceba-1145-46b2-bcfd-7b58ab31a865")
    }
}