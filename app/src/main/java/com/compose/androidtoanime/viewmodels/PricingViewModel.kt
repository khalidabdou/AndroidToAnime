package com.compose.androidtoanime.viewmodels


import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.AppUtils.Companion.TAG_BILLING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PricingViewModel @Inject constructor(
    private val repo: RepositoryImpl,
    application: Application
) : AndroidViewModel(application) {

    private val _purchasedSubscriptionsLiveData =
        repo.pricingRepositoryImpl.getBillingUpdateListener().purchaseUpdateLiveData
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

    fun listener() {
        Log.d(TAG_BILLING, "listner" + _purchasedSubscriptionsLiveData.value.toString())
    }


    fun ask() = viewModelScope.launch {
       // repo.pricingRepositoryImpl.acknowledgePurchase(productList[0]!!.subscriptionOfferDetails)
    }





}
