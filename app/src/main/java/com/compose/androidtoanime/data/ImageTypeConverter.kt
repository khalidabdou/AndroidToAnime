package com.compose.androidtoanime.data

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class imageTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun ResponsePhotoToString(image: ResponsePhoto): String {
        return gson.toJson(image)
    }

    @TypeConverter
    fun StringToResponsePhoto(data: String): ResponsePhoto {
        var listType = object : TypeToken<ResponsePhoto>() {}.type
        return gson.fromJson(data, listType)
    }


}