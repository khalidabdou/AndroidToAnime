package com.compose.androidtoanime.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.androidtoanime.Utils.AppUtils.Companion.TABLE_IMAGE
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = TABLE_IMAGE)
data class ResponsePhoto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("folder")
    val folder: String,
    @SerializedName("filename")
    val filename: String
) : Parcelable
