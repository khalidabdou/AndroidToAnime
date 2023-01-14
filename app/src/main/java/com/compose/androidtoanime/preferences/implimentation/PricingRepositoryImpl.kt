package com.compose.androidtoanime.preferences.implimentation

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING
import com.compose.androidtoanime.Utils.connect
import com.compose.androidtoanime.Utils.consumeProduct
import com.compose.androidtoanime.Utils.getProducts
import com.compose.androidtoanime.data.BillingUpdateListener
import com.compose.androidtoanime.data.model.BillingClientProvider
import com.compose.androidtoanime.preferences.abstraction.PricingRepository
import com.google.common.collect.ImmutableList
import com.lucianoluzzi.firebase_test.domain.model.ConsumeProductResult
import javax.inject.Inject


class PricingRepositoryImpl @Inject constructor(
    billingClientProvider: BillingClientProvider
) : PricingRepository {

    private val billingClientProvider = billingClientProvider
    private val billingClient = billingClientProvider.billingClient


    override suspend fun getPurchases(purchaseType: String): List<Purchase>? {
        val connectIfNeeded = connectIfNeeded()
        if (!connectIfNeeded)
            return null

        return null
    }

    override suspend fun getProducts(billingClient: BillingClient): List<ProductDetails>? {
        val connectIfNeeded = connectIfNeeded()
        if (!connectIfNeeded)
            return null

        return billingClient.getProducts(billingClient)
    }

    private suspend fun connectIfNeeded(): Boolean {
        return billingClient.isReady || billingClient.connect()
    }

    override suspend fun consumeProduct(consumeParams: ConsumeParams): ConsumeProductResult =
        billingClient.consumeProduct(consumeParams)


//    override suspend fun acknowledgePurchase(acknowledgePurchaseParams: AcknowledgePurchaseParams): BillingResult =
//        billingClient.acknowledgePurchase(acknowledgePurchaseParams)

    override fun getBillingClient() = billingClient

    override fun makePurchase(activity: Activity, productDetails: ProductDetails) {
        val offerToken = productDetails.subscriptionOfferDetails?.get(0)?.offerToken
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                ImmutableList.of(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offerToken!!)
                        .build()
                )
            )
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun getBillingUpdateListener(): BillingUpdateListener {
        return this.billingClientProvider.billingUpdateListener
    }

    override suspend fun acknowledgePurchase(purchase: Purchase?): BillingResult? {
        var billingResult1: BillingResult? = null
        purchase?.let {
            if (!it.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(
                    params
                ) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        Log.d(TAG_BILLING, "true " + it.toString())
                        billingResult1 = billingResult
                    }
                    Log.d(TAG_BILLING, "false " + it.toString())
                }
            }
        }
        return billingResult1
    }

}