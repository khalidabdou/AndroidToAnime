package com.wishes.jetpackcompose.data




import com.compose.androidtoanime.data.ResponsePhoto
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

    suspend fun upload(photo : MultipartBody.Part): Response<ResponsePhoto?> {
        return wallApi.sendPhotoFromBody(photo)
    }


    suspend fun getAds()=wallApi.getAds()


}