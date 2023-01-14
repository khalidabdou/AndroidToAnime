package com.compose.androidtoanime.data.model

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.compose.androidtoanime.data.BillingUpdateListener
import javax.inject.Inject


class BillingClientProvider @Inject constructor(
    context: Context,
     billingUpdateListener: BillingUpdateListener
) {

    var billingClient = BillingClient.newBuilder(context)
        .setListener(billingUpdateListener)
        .enablePendingPurchases()
        .build()
}