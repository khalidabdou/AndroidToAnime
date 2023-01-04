package com.wishes.jetpackcompose.data




import com.compose.androidtoanime.data.entities.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val wallApi: Api
) {
    // images
    suspend fun test(): Response<String?>? {
        return wallApi.test()
    }

    suspend fun upload(photo : MultipartBody.Part): Response<String?> {
        return wallApi.sendPhotoFromBody(photo)
    }


}