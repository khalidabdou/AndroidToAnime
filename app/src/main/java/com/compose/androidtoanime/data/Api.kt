package com.compose.androidtoanime.data.entities

import com.compose.androidtoanime.data.ResponsePhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface Api {

    @Multipart
    @POST("/upload")
    suspend fun sendPhotoFromBody(@Part files: MultipartBody.Part): Response<ResponsePhoto?>

    @POST("trpc.shadow_cv.ai_processor_cgi.AIProcessorCgi/Process")
    suspend fun test(
        @HeaderMap headers: Map<String, String>, @Body body: String
    ): Response<Any?>?

}