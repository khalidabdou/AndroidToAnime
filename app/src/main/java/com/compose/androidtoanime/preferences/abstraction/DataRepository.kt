package com.wishes.jetpackcompose.preferences.abstraction

import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow


interface DataRepository {
    suspend fun savePurchase(key: String, value: Purchase)
    suspend fun getPurchase(key: String): String?
    suspend fun incrementConvertCount()
    suspend fun getConvertCount(): Flow<Int>

}