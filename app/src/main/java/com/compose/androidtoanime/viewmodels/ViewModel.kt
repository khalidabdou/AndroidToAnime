package com.compose.androidtoanime.viewmodels


import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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


}