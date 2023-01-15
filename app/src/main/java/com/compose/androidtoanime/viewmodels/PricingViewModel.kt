package com.compose.androidtoanime.viewmodels


import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING
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

    var purches: MutableState<List<Purchase>?> =
        repo.pricingRepositoryImpl.getBillingUpdateListener().purchaseUpdateLiveData
    var isSubscribe = mutableStateOf<Boolean>(false)

    val billingClient = repo.pricingRepositoryImpl.getBillingClient()
    var productList = mutableListOf<ProductDetails?>()

    fun getProducts() {
        viewModelScope.launch {
            productList = repo.pricingRepositoryImpl.getProducts(billingClient)!!.toMutableList()
            Log.d(TAG_BILLING, productList.toString())
        }
    }


    fun makePurchase(activity: Activity) =
        repo.pricingRepositoryImpl.makePurchase(activity, productList[0]!!)


    fun acknowledgePurchase() = runBlocking {

        val gson = Gson()
        val purchase = repo.dataStore.getPurchase("purchase")
        val json = purchase
        val saved = gson.fromJson(json, Purchase::class.java)

        if (saved == null) {
            Log.d(TAG_BILLING, "purchased is not found")
            return@runBlocking
        }

        Log.d(TAG_BILLING, "saved $saved")
        //var result: BillingResult? = null

        //val result = handlePurchase()
        //Log.d(TAG_BILLING, " results $result")
        //isSubscribe.value = result != null

    }

    fun handlePurchase() = runBlocking {
        val gson = Gson()
        val purchase = repo.dataStore.getPurchase("purchase")
        val json = purchase
        val saved = gson.fromJson(json, Purchase::class.java)
        if (saved == null) {
            Log.d(TAG_BILLING, "purchased is not found")
            return@runBlocking
        }
        isSubscribe.value =
            repo.pricingRepositoryImpl.acknowledgePurchase(saved, billingClient) == true

    }

    fun savePurchase(purchase: Purchase) = viewModelScope.launch {
        repo.dataStore.savePurchase("purchase", purchase)
    }


}


