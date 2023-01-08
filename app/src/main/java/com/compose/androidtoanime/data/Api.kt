package com.compose.androidtoanime.data.entities

import com.compose.androidtoanime.data.Ads
import com.compose.androidtoanime.data.ResponsePhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface Api {

    @Multipart
    @POST("/upload")
    suspend fun sendPhotoFromBody(@Part files: MultipartBody.Part): Response<ResponsePhoto?>

    //ads
    @GET("/api/ads")
    suspend fun getAds(
    ): Response<Ads?>

}