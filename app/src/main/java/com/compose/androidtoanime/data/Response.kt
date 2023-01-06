package com.compose.androidtoanime.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponsePhoto(
    @SerializedName("folder")
    val folder: String,
    @SerializedName("filename")
    val filename: String
) : Parcelable
