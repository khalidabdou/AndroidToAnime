package com.wishes.jetpackcompose.data




import com.compose.androidtoanime.data.entities.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.ByteString.Companion.encode
import retrofit2.Response
import java.security.MessageDigest
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val wallApi: Api
) {
    // images
    suspend fun test(body: String): Response<Any?>? {

        val requestData = "request data"
        val signValue = MessageDigest
            .getInstance("MD5")
            .apply { update("https://h5.tu.qq.com${requestData.length}HQ31X02e".toByteArray()) }
            .digest()
            .joinToString("") { String.format("%02x", it) }
        println(signValue)

        val requestHeaders = mapOf(
            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",
            "accept" to "application/json",
            "content-type" to "application/json",
            "origin" to "https://h5.tu.qq.com",
            "x-sign-version" to "v1",
            "x-sign-value" to signValue
        )

        val headers = mapOf(
            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",
            "accept" to "application/json",
            "content-type" to "application/json",
            "origin" to "https://h5.tu.qq.com",
            "x-sign-version" to "v1",
           // "x-sign-value" to hashlib.md5("https://h5.tu.qq.com${1}HQ31X02e".encode()).hexdigest()
        )

        return wallApi.test(requestHeaders,body)
    }

    suspend fun upload(photo : MultipartBody.Part): Response<String?> {
        return wallApi.sendPhotoFromBody(photo)
    }


}