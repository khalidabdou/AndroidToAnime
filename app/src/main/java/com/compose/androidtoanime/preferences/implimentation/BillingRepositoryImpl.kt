package com.compose.androidtoanime.preferences.implimentation

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.compose.androidtoanime.preferences.abstraction.BillingRepository

class BillingRepositoryImpl :BillingRepository {
    override fun getBillingClient(): BillingClient {
        TODO("Not yet implemented")
    }

    override suspend fun getPurchasedSubscriptions(): List<Purchase>? {
        TODO("Not yet implemented")
    }

    override suspend fun getSubscriptions(): List<SkuDetails>? {
        TODO("Not yet implemented")
    }

    override suspend fun consumePurchase(purchase: Purchase) {
        TODO("Not yet implemented")
    }

    override suspend fun acknowledgePurchase(purchase: Purchase): BillingResult? {
        TODO("Not yet implemented")
    }
}