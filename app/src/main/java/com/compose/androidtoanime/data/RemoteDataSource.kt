package com.wishes.jetpackcompose.data


import com.compose.androidtoanime.data.Ads
import com.compose.androidtoanime.data.ResponsePhoto
import com.compose.androidtoanime.data.entities.Api
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val wallApi: Api
) {

    suspend fun upload(photo: MultipartBody.Part): Response<ResponsePhoto?> {
        return wallApi.sendPhotoFromBody(photo)
    }

    suspend fun premium(photo: MultipartBody.Part): Response<ResponsePhoto?> {
        return wallApi.premium(photo)
    }

    suspend fun getAds(): Response<Ads?> = wallApi.getAds()


}