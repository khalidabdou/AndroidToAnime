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
                Log.d(TAG_BILLING, "onBillingServiceDisconnected")
                //continuation.resume(false)
            }
        })
    }
}

fun BillingClient.startBillingConnection() {
    this.startConnection(object : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                Log.d(TAG_BILLING, billingResult.responseCode.toString())
                Log.d(TAG_BILLING, "Billing response OK")


            } else {
                Log.d(TAG_BILLING, "Billing response OK 2")
                Log.e(TAG_BILLING, billingResult.debugMessage)
            }
        }

        override fun onBillingServiceDisconnected() {
            Log.i(TAG_BILLING, "Billing connection disconnected")
            startBillingConnection()
        }
    })
}


suspend fun BillingClient.acknowledgePurchase(
    purchase: Purchase,
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

suspend fun BillingClient.checkSubscription(): List<Purchase>? {
    return suspendCancellableCoroutine { continuation ->
        this.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()
        ) { billingResult1, purchaseList ->
            if (billingResult1.getResponseCode() === BillingClient.BillingResponseCode.OK) {
                continuation.resume(purchaseList)
            } else
                continuation.resume(null)
        }
    }


}

