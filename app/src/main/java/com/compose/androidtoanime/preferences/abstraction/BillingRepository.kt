package com.compose.androidtoanime.preferences.abstraction

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

interface BillingRepository {

    fun getBillingClient(): BillingClient

    suspend fun getPurchasedSubscriptions(): List<Purchase>?

    suspend fun getSubscriptions(): List<SkuDetails>?

    suspend fun consumePurchase(purchase: Purchase)

    suspend fun acknowledgePurchase(purchase: Purchase): BillingResult?


}