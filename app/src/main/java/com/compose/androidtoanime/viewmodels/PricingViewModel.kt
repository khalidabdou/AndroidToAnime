package com.compose.androidtoanime.viewmodels


import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING

import com.compose.androidtoanime.Utils.startBillingConnection
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class PricingViewModel @Inject constructor(
    private val repo: RepositoryImpl,
    application: Application
) : AndroidViewModel(application) {

    var purchaes: MutableState<List<Purchase>?> =
        repo.pricingRepositoryImpl.getBillingUpdateListener().purchaseUpdateLiveData
    var isSubscribe = mutableStateOf<Boolean>(false)

    var billingClient = repo.pricingRepositoryImpl.getBillingClient()
    var productList = mutableListOf<ProductDetails?>()


    fun getProducts() {
        viewModelScope.launch {
            if (repo.pricingRepositoryImpl.getProducts(billingClient) != null)
                productList =
                    repo.pricingRepositoryImpl.getProducts(billingClient)!!.toMutableList()
            Log.d(TAG_BILLING, "$productList")
        }
    }

    init {
        //isSubscribe.value = !purchaes.value.isNullOrEmpty()
        //handlePurchase()
    }


    fun makePurchase(activity: Activity) =
        repo.pricingRepositoryImpl.makePurchase(activity, productList[0]!!)


    fun handlePurchase() = runBlocking {
        val gson = Gson()
        val purchase = repo.dataStore.getPurchase("purchase")
        val saved = gson.fromJson(purchase, Purchase::class.java)
        if (saved == null) {
            Log.d(TAG_BILLING, "purchased is not found")
            return@runBlocking
        }
        verifySubPurchase(saved)
        //val isp=repo.pricingRepositoryImpl.acknowledgePurchase(saved)
        Log.d(TAG_BILLING, "saved $saved")
        //Log.d(TAG_BILLING, "results $isp")
        //isSubscribe.value =

        val params = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)


    }

    fun savePurchase(purchase: Purchase) = viewModelScope.launch {
        repo.dataStore.savePurchase("purchase", purchase)
    }

    fun verifySubPurchase(purchases: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(
            acknowledgePurchaseParams
        ) { billingResult: BillingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                isSubscribe.value = true
                Log.d(TAG_BILLING, "Subscription activated, Enjoy!" + purchases.purchaseToken)
            }
        }
        Log.d(TAG_BILLING, "Purchase Token: " + purchases.purchaseToken)
        Log.d(TAG_BILLING, "Purchase Time: " + purchases.purchaseTime)
        Log.d(TAG_BILLING, "Purchase OrderID: " + purchases.orderId)
    }

    init {
        //getProducts()
        checkSubscription()
    }

    fun checkSubscription() {
        viewModelScope.launch {
            purchaes.value = repo.pricingRepositoryImpl.checkSubscription()
            if (!purchaes.value.isNullOrEmpty()) {
                verifySubPurchase(purchaes.value!![0])
            }

            Log.d(TAG_BILLING, "checkSubscription ${purchaes.value}")
        }

    }

    fun startConnection() {
        if (!billingClient.isReady)
            viewModelScope.launch {
                billingClient.startBillingConnection()
            }

    }


}


