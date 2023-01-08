package com.compose.androidtoanime.data

import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface IDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(images: ResponsePhoto)

    @Query("SELECT * FROM table_photos")
    fun getPhotos(): Flow<List<ResponsePhoto>>

}