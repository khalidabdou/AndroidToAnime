package com.compose.androidtoanime.data.model

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.compose.androidtoanime.data.BillingUpdateListener
import javax.inject.Inject


class BillingClientProvider @Inject constructor(
    context: Context,
    billingUpdateListener: BillingUpdateListener
) {
    var billingUpdateListener = billingUpdateListener
    var billingClient = BillingClient.newBuilder(context)
        .setListener(billingUpdateListener)
        .enablePendingPurchases()
        .build()
}