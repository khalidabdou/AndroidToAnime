package com.compose.androidtoanime.data.model

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BillingClientProvider @Inject constructor(
     context: Context,
    updateListener: PurchasesUpdatedListener
) {
    val billingClient = BillingClient
        .newBuilder(context)
        .enablePendingPurchases()
        .setListener(updateListener)
        .build()
}