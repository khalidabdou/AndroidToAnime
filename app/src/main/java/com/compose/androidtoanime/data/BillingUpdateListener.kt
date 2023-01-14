package com.compose.androidtoanime.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING
import javax.inject.Inject

class BillingUpdateListener @Inject constructor() : PurchasesUpdatedListener {

    private val _purchaseUpdateLiveData = MutableLiveData<List<Purchase>?>()
    val purchaseUpdateLiveData: LiveData<List<Purchase>?> = _purchaseUpdateLiveData

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        Log.d(TAG_BILLING,"${billingResult}")
        _purchaseUpdateLiveData.value = purchases
    }
}