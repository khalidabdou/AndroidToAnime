package com.compose.androidtoanime.viewmodels


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.*
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.AppUtils.Companion.bitmap
import com.compose.androidtoanime.Utils.AppUtils.Companion.generateNewPath
import com.compose.androidtoanime.Utils.AppUtils.Companion.saveBitmapToFile
import com.compose.androidtoanime.Utils.HandleResponse
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.data.AdProvider.Companion.Banner
import com.compose.androidtoanime.data.AdProvider.Companion.BannerApplovin
import com.compose.androidtoanime.data.AdProvider.Companion.BannerFAN
import com.compose.androidtoanime.data.AdProvider.Companion.Inter
import com.compose.androidtoanime.data.AdProvider.Companion.InterApplovin
import com.compose.androidtoanime.data.AdProvider.Companion.InterFAN
import com.compose.androidtoanime.data.AdProvider.Companion.OpenAd
import com.compose.androidtoanime.data.AdProvider.Companion.Rewarded
import com.compose.androidtoanime.data.Ads
import com.compose.androidtoanime.data.ResponsePhoto
import com.google.common.collect.ImmutableList
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QEntitlement
import com.qonversion.android.sdk.dto.QEntitlementRenewState
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.offerings.QOffering
import com.qonversion.android.sdk.dto.offerings.QOfferings
import com.qonversion.android.sdk.dto.products.QProduct
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback
import com.qonversion.android.sdk.listeners.QonversionOfferingsCallback
import com.qonversion.android.sdk.listeners.QonversionProductsCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.nio.file.Files
import java.util.*
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class ViewModel @Inject constructor(

    private val repo: RepositoryImpl,
    application: Application

) : AndroidViewModel(application) {

    //local
    var myPhotos by mutableStateOf(emptyList<ResponsePhoto>())

    //remote
    var readyImage by mutableStateOf<NetworkResults<ResponsePhoto>?>(NetworkResults.NotYet())
    val infos = mutableStateOf<NetworkResults<Ads>>(NetworkResults.Loading())

    //premium

    //variables
    var openPremium by mutableStateOf(false)
    var navigateClick by mutableStateOf(false)
    var openExit by mutableStateOf(false)
    var openHow by mutableStateOf(false)
    var image=""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Recycle")
    fun upload(path: String) = viewModelScope.launch {

        val fileName = "${generateNewPath()}+.jpeg"
        val newpath = path.replace(path.split(File.separator).last(), fileName)

        val fileTemp = File(newpath)
        Files.createFile(fileTemp.toPath())

        val file = saveBitmapToFile(bitmap, fileTemp)

        readyImage = NetworkResults.Loading()
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val body = MultipartBody.Part.createFormData("file", file!!.name, requestFile)
        val res = repo.remote.upload(body)
        readyImage = HandleResponse(res).handleResult()
        if (readyImage is NetworkResults.Success){
            if (readyImage!=null && (readyImage as NetworkResults.Success<ResponsePhoto>).data!=null)
            insertPhoto(readyImage!!.data!!)
        }

    }

    suspend fun getPhotos() = withContext(Dispatchers.Main){
        //Log.d("ads", "begin==p===")
        repo.localData.getPhotos().collect {
            myPhotos = it
        }
    }

    private fun insertPhoto(photo: ResponsePhoto) = viewModelScope.launch {
        repo.localData.insertPhoto(photo)
    }


    fun getAds() = viewModelScope.launch {
        Log.d("ads", "begin==p===")
        val res=repo.remote.getAds()
        infos.value= HandleResponse(res).handleResult()
        when(infos.value){
            is NetworkResults.Error ->{
                Log.d("ads", "err")
            }
            is NetworkResults.Success ->{
                    infos.value.data!!.ads.forEach {
                        Log.d("FAN", it.ad_id)
                        when (it.type) {
                            "banner" -> {
                                Banner = it
                                Log.d("ads", Banner.toString())
                            }
                            "inter" -> {
                                Inter = it
                                Log.d("ads", Inter.toString())
                            }
                            "open" -> {
                                OpenAd = it
                                Log.d("ads", OpenAd.toString())
                            }
                            "rewarded" -> {
                                Rewarded = it
                                //Log.d("ads", OpenAd.toString())
                            }
                            "banner_fan" -> {
                                Log.d("FAN", it.ad_id)
                                BannerFAN = it
                                //Log.d("ads", Banner.toString())
                            }
                            "inter_fan" -> {
                                InterFAN = it
                                //Log.d("ads", Inter.toString())
                            }
                            "banner_applovin" -> {
                                Log.d("FAN", it.ad_id)
                                BannerApplovin = it
                                //Log.d("ads", Banner.toString())
                            }
                            "inter_Applovin" -> {
                                InterApplovin = it
                                //Log.d("ads", Inter.toString())
                            }
                        }
                    }
            }
        }
    }

    fun deleteFromRoom(photo: ResponsePhoto) = viewModelScope.launch(Dispatchers.Main) {
        repo.localData.delete(photo)
    }


    fun getUrl():String{
        val data = readyImage!!.data
        image =BuildConfig.api + data?.folder + "crop" + data?.filename
        return image
    }



    //premium
    val TAG="Qonv"
    val TAG2="Gonv"
    var offerings by mutableStateOf<List<QOffering>>(emptyList())
        private set

    var hasPremiumPermission by mutableStateOf(false)
        private set

    var productsQ by mutableStateOf<QProduct?>(null)

    init {
        //updatePermissions()
        loadOfferings()
        loadProducts()
        hasPermission()
    }

    private fun loadOfferings() {
        Qonversion.shared.offerings(object: QonversionOfferingsCallback {
            override fun onSuccess(offerings: QOfferings) {
                val mainOffering = offerings.main
                if (mainOffering != null && mainOffering.products.isNotEmpty()) {
                    // Display products for sale
                    this@ViewModel.offerings = offerings.availableOfferings
                    Log.d(TAG, "success: ${this@ViewModel.offerings.size}")
                }
            }
            override fun onError(error: QonversionError) {
                Log.d(TAG, "error: ${error}")
                // handle error here
            }
        })

    }

    private fun loadProducts(){
        Qonversion.shared.products(callback = object: QonversionProductsCallback {
            override fun onSuccess(products: Map<String, QProduct>) {
                // handle available products here
                productsQ = products.get("premium_product")
                Log.d(TAG, "success: ${productsQ}")
            }

            override fun onError(error: QonversionError) {
                // handle error here
            }
        })
    }

    fun purchase(context: Activity) {
        Qonversion.shared.purchase(context,productsQ!!,object : QonversionEntitlementsCallback{
            override fun onError(error: QonversionError) {
                Toast.makeText(context,"err:${error}",Toast.LENGTH_SHORT).show()
                Log.d(TAG,"err:${error}")
            }

            override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                Log.d(TAG,"err:${entitlements}")
                Toast.makeText(context,"err:${entitlements}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun hasPermission(){
        Qonversion.shared.checkEntitlements(object: QonversionEntitlementsCallback {
            override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                val premiumEntitlement = entitlements["premium"]
                if (premiumEntitlement != null && premiumEntitlement.isActive) {
                    // handle active entitlement here
                    Log.d(TAG,"premium")
                }else
                    Log.d(TAG,"no premium")
            }

            override fun onError(error: QonversionError) {
                Log.d(TAG,"err")
                // handle error here
            }
        })

    }

    val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }



    fun startConnexion(context: Context){
        //Toast.makeText(context,"start",Toast.LENGTH_SHORT).show()
        var billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
                    // The BillingClient is ready. You can query purchases here.
                }else
                    Toast.makeText(context,"${billingResult.responseCode}",Toast.LENGTH_SHORT).show()
            }
            override fun onBillingServiceDisconnected() {
                Toast.makeText(context,"dis",Toast.LENGTH_SHORT).show()
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun getProducts(context:Context){
        startConnexion(context)
        var billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
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

            Log.d(TAG2,"kkkkk")
            Log.d(TAG2,billingResult.toString())
            // check billingResult
            // process returned productDetailsList
        }


    }



}