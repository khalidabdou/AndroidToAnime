package com.compose.androidtoanime.preferences.abstraction

import android.app.Activity
import com.android.billingclient.api.*
import com.lucianoluzzi.firebase_test.domain.model.ConsumeProductResult

interface PricingRepository {
    suspend fun getPurchases(purchaseType: String): List<Purchase>?
    suspend fun getProducts(billingClient: BillingClient): List<ProductDetails>?
    suspend fun consumeProduct(consumeParams: ConsumeParams): ConsumeProductResult
    //suspend fun acknowledgePurchase(acknowledgePurchaseParams: AcknowledgePurchaseParams): BillingResult
    fun getBillingClient(): BillingClient
    fun makePurchase(activity: Activity,productDetails: ProductDetails)
}
