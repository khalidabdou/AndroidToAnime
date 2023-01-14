package com.compose.androidtoanime.Utils

import com.android.billingclient.api.*
import com.google.common.collect.ImmutableList
import com.lucianoluzzi.firebase_test.domain.model.ConsumeProductResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun BillingClient.connect(): Boolean {
    return suspendCancellableCoroutine { continuation ->
        startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    continuation.resume(true)
                } else {
                    continuation.resume(false)
                }
            }

            override fun onBillingServiceDisconnected() {
                continuation.resume(false)
            }
        })
    }
}

suspend fun BillingClient.getProducts(billingClient:BillingClient): List<ProductDetails>? {
    return suspendCancellableCoroutine { continuation ->

        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("premium_access")
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()))
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) {
                billingResult,
                productDetailsList ->
            // check billingResult
            // process returned productDetailsList
            continuation.resume(productDetailsList)
        }
    }
}

suspend fun BillingClient.consumeProduct(consumeParams: ConsumeParams): ConsumeProductResult {
    return suspendCancellableCoroutine { continuation ->
        consumeAsync(consumeParams) { billingResult, purchaseToken ->
            continuation.resume(
                ConsumeProductResult(
                    billingResult = billingResult,
                    purchaseToken = purchaseToken
                )
            )
        }
    }
}