package com.compose.androidtoanime.data.entities

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface Api {

    @POST("/upload")
    suspend fun sendPhotoFromBody(@Body image: MultipartBody.Part): Response<String?>

    @GET("/")
    suspend fun test(
    ): Response<String?>?

}