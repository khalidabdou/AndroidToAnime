package com.compose.androidtoanime.preferences.implimentation

import com.android.billingclient.api.*
import com.compose.androidtoanime.Utils.connect
import com.compose.androidtoanime.Utils.consumeProduct
import com.compose.androidtoanime.Utils.getProducts
import com.compose.androidtoanime.data.model.BillingClientProvider

import com.compose.androidtoanime.preferences.abstraction.PricingRepository

import com.lucianoluzzi.firebase_test.domain.model.ConsumeProductResult
import javax.inject.Inject


class PricingRepositoryImpl @Inject constructor(
    billingClientProvider: BillingClientProvider
) : PricingRepository {

    private val billingClient = billingClientProvider.billingClient

    override suspend fun getPurchases(purchaseType: String): List<Purchase>? {
        val connectIfNeeded = connectIfNeeded()
        if (!connectIfNeeded)
            return null

        return billingClient.queryPurchases(purchaseType).purchasesList
    }

    override suspend fun getProducts(productDetailsParams: SkuDetailsParams): List<SkuDetails>? {
        val connectIfNeeded = connectIfNeeded()
        if (!connectIfNeeded)
            return null

        return billingClient.getProducts(productDetailsParams)
    }

    private suspend fun connectIfNeeded(): Boolean {
        return billingClient.isReady || billingClient.connect()
    }

    override suspend fun consumeProduct(consumeParams: ConsumeParams): ConsumeProductResult =
        billingClient.consumeProduct(consumeParams)

    override suspend fun acknowledgePurchase(acknowledgePurchaseParams: AcknowledgePurchaseParams): BillingResult =
        billingClient.acknowledgePurchase(acknowledgePurchaseParams)

    override fun getBillingClient() = billingClient
}