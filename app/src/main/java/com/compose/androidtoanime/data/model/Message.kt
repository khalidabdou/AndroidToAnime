package com.compose.androidtoanime.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Message(
    @SerializedName("message")
    val text: String,
    val sender: String,
    val timestamp: String
) : Parcelable

class Messages(
    var messages: ArrayList<Message> = ArrayList()
)
