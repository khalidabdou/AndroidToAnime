package com.compose.androidtoanime.Utils

import android.util.Log
import com.android.billingclient.api.*
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING
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


suspend fun BillingClient.acknowledgePurchase(
    purchase: Purchase,
    billingClient: BillingClient
): Boolean? {

    return suspendCancellableCoroutine { continuation ->
        val acknowledgePurchaseResponseListener =
            AcknowledgePurchaseResponseListener {
                if (it.responseCode.equals("ITEM_NOT_OWNED")) {
                    continuation.resume(false)
                } else
                    continuation.resume(true)
                Log.d(TAG_BILLING, "AcknowledgePurchaseResponseListener $it")
            }
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                val ackPurchaseResult =
                    this.acknowledgePurchase(
                        acknowledgePurchaseParams.build(),
                        acknowledgePurchaseResponseListener
                    )

                Log.d(TAG_BILLING, "ackPurchaseResult $ackPurchaseResult")
            }
        }
    }
}


suspend fun BillingClient.getProducts(billingClient: BillingClient): List<ProductDetails>? {
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
            Log.d(TAG_BILLING, "get products $billingResult")
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

