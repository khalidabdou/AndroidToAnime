package com.compose.androidtoanime.data.entities

import com.compose.androidtoanime.data.Ads
import com.compose.androidtoanime.data.ResponsePhoto
import com.compose.androidtoanime.data.model.Message
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface Api {

    @Multipart
    @POST("/upload")
    suspend fun convert(@Part files: MultipartBody.Part): Response<ResponsePhoto?>

    @Multipart
    @POST("/api/premium")
    suspend fun premium(@Part files: MultipartBody.Part): Response<ResponsePhoto?>

    //ads
    @GET("/api/ads")
    suspend fun getAds(
    ): Response<Ads?>

    @Multipart
    @POST("/api/chat")
    suspend fun sendMessage(@Part body: MultipartBody.Part): Response<Message?>

}