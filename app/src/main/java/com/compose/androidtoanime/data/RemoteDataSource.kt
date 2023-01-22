package com.wishes.jetpackcompose.data


import com.compose.androidtoanime.data.Ads
import com.compose.androidtoanime.data.ResponsePhoto
import com.compose.androidtoanime.data.entities.Api
import com.compose.androidtoanime.data.model.Message
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val wallApi: Api
) {

    suspend fun convert(photo: MultipartBody.Part): Response<ResponsePhoto?> {
        return wallApi.convert(photo)
    }

    suspend fun premium(photo: MultipartBody.Part): Response<ResponsePhoto?> {
        return wallApi.premium(photo)
    }

    suspend fun getAds(): Response<Ads?> = wallApi.getAds()

    suspend fun sendMessage(message: MultipartBody.Part): Response<Message?> =
        wallApi.sendMessage(message)


}