package com.compose.androidtoanime.data.entities

import com.compose.androidtoanime.data.Ads
import com.compose.androidtoanime.data.ResponsePhoto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface Api {

    @Multipart
    @POST("/upload")
    suspend fun sendPhotoFromBody(@Part files: MultipartBody.Part): Response<ResponsePhoto?>

    @Multipart
    @POST("/api/premium")
    suspend fun premium(@Part files: MultipartBody.Part): Response<ResponsePhoto?>

    //ads
    @GET("/api/ads")
    suspend fun getAds(
    ): Response<Ads?>

}