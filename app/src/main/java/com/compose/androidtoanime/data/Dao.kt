package com.compose.androidtoanime.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface IDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(images: ResponsePhoto)

    @Query("SELECT * FROM table_photos ORDER BY id desc")
    fun getPhotos(): Flow<List<ResponsePhoto>>

    @Delete
    suspend fun delete(images: ResponsePhoto)

}