package com.compose.androidtoanime.preferences.abstraction

import com.android.billingclient.api.*
import com.lucianoluzzi.firebase_test.domain.model.ConsumeProductResult

interface BillingRepository {

    fun getBillingClient(): BillingClient

    suspend fun getPurchasedSubscriptions(): List<Purchase>?

    suspend fun getSubscriptions(): List<SkuDetails>?

    suspend fun consumePurchase(purchase: Purchase)

    suspend fun acknowledgePurchase(purchase: Purchase): BillingResult?


}