package com.compose.androidtoanime

import com.android.billingclient.api.BillingClient
import com.compose.androidtoanime.data.LocalDataSource
import com.compose.androidtoanime.preferences.implimentation.DataStoreRepositoryImpl
import com.compose.androidtoanime.preferences.implimentation.PricingRepositoryImpl

import com.wishes.jetpackcompose.data.RemoteDataSource

import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localData: LocalDataSource,
    dataStore: DataStoreRepositoryImpl,
    pricingRepositoryImpl: PricingRepositoryImpl,
    billingClientProvider : BillingClient

) {
    val remote = remoteDataSource
    val localData =localData
    val dataStore = dataStore
    val pricingRepositoryImpl = pricingRepositoryImpl
    val billingClientProvider = billingClientProvider


}