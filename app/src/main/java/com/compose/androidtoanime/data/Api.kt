package com.compose.androidtoanime.data.entities

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface Api {

    @POST("/")
    suspend fun sendPhotoFromBody(@Body photo: RequestBody): Response<String?>

    @GET("images")
    suspend fun getImages(
        @Query("language") language: Int = 3
    ): Response<String?>?

}