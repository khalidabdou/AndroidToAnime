package com.compose.androidtoanime.data



import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val iDao: IDao,

) {


    suspend fun insertPhoto(photo: ResponsePhoto)= iDao.insertPhoto(photo)
    fun getPhotos():Flow<List<ResponsePhoto>> = iDao.getPhotos()

    suspend fun delete(photo: ResponsePhoto)=iDao.delete(photo)



}