package com.wishes.jetpackcompose.data




import com.compose.androidtoanime.data.entities.Api
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val wallApi: Api
) {
    // images
    suspend fun getImages(languageApp: Int): Response<String?>? {
        return wallApi.getImages(languageApp)
    }

    suspend fun getCategories(cat :RequestBody): Response<String?> {
        return wallApi.sendPhotoFromBody(cat)
    }


}