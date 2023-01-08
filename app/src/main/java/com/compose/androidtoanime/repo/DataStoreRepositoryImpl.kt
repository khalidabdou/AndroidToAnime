package com.compose.androidtoanime

import com.compose.androidtoanime.data.LocalDataSource
import com.compose.androidtoanime.preferences.implimentation.DataStoreRepositoryImpl
import com.wishes.jetpackcompose.data.RemoteDataSource

import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localData: LocalDataSource,
    dataStore: DataStoreRepositoryImpl
) {
    val remote = remoteDataSource
    val localData =localData
    val dataStore = dataStore

}