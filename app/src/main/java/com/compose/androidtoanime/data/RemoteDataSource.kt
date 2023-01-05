package com.wishes.jetpackcompose.data




import com.compose.androidtoanime.data.entities.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.ByteString.Companion.encode
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val wallApi: Api
) {
    // images
    suspend fun test(body: String): Response<Any?>? {
        val headers = mapOf(
            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",
            "accept" to "application/json",
            "content-type" to "application/json",
            "origin" to "https://h5.tu.qq.com",
            "x-sign-version" to "v1",
            //"x-sign-value" to hashlib.md5("https://h5.tu.qq.com${1}HQ31X02e".encode()).hexdigest()
        )

        return wallApi.test(headers,body)
    }

    suspend fun upload(photo : MultipartBody.Part): Response<String?> {
        return wallApi.sendPhotoFromBody(photo)
    }


}