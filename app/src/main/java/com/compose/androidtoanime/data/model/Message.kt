package com.compose.androidtoanime.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Message(
    @SerializedName("message")
    var text: String,
    val sender: String?,
    var timestamp: String?
) : Parcelable

class Messages(
    var messages: ArrayList<Message> = ArrayList()
)
