package com.compose.androidtoanime.viewmodels


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.R
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
import com.compose.androidtoanime.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: RepositoryImpl,
    application: Application

) : AndroidViewModel(application) {

    //local
    var myPhotos by mutableStateOf(emptyList<ResponsePhoto>())

    var _message by mutableStateOf<NetworkResults<Message>>(NetworkResults.NotYet())
    var messages = mutableStateListOf(Message("How can I help you", "AnimeBot", "Now"))
    //val messageState= mutableStateOf(NetworkResults.NotYet())

    //remote
    var readyImage by mutableStateOf<NetworkResults<ResponsePhoto>?>(NetworkResults.NotYet())
    val infos = mutableStateOf<NetworkResults<Ads>>(NetworkResults.Loading())

    //variables
    var openPremium by mutableStateOf(false)
    var navigateClick by mutableStateOf(false)
    var openExit by mutableStateOf(false)
    var openHow by mutableStateOf(false)
    var image = ""
    var isSubscribe = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Recycle")
    fun convert(path: String) = viewModelScope.launch {

        val fileName = "${generateNewPath()}+.jpeg"
        val newpath = path.replace(path.split(File.separator).last(), fileName)

        val fileTemp = File(newpath)
        Files.createFile(fileTemp.toPath())
        val file = saveBitmapToFile(bitmap, fileTemp)
        readyImage = NetworkResults.Loading()
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
        val body = MultipartBody.Part.createFormData("file", file!!.name, requestFile)
        val res = if (!isSubscribe.value) repo.remote.premium(body) else repo.remote.premium(body)
        readyImage = HandleResponse(res).handleResult()
        if (readyImage is NetworkResults.Success) {
            if (readyImage != null && (readyImage as NetworkResults.Success<ResponsePhoto>).data != null)
                insertPhoto(readyImage!!.data!!)
        }
    }

    suspend fun getPhotos() = withContext(Dispatchers.Main) {
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
        val res = repo.remote.getAds()
        infos.value = HandleResponse(res).handleResult()
        when (infos.value) {
            is NetworkResults.Error -> {
                Log.d("ads", "err")
            }
            is NetworkResults.Success -> {
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

    fun getUrl(): String {
        val data = readyImage!!.data
        image = BuildConfig.api + data?.folder + "crop" + data?.filename
        return image
    }

    fun incrementCount() {
        viewModelScope.launch {
            repo.dataStore.incrementConvertCount()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(isSubscribe: Boolean, message: Message, context: Context) {

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val current = LocalDateTime.now().format(formatter)

        message.timestamp = "$current"
        messages.add(message)
        if (messages.size > 15 && !isSubscribe) {
            val subscribe = context.resources.getStringArray(R.array.subscribe_messages)

            _message = NetworkResults.Loading()
            val subscribeMessage = Message(
                subscribe.random(),
                "AnimeBot",
                current
            )
            viewModelScope.launch {
                delay(2000)
                messages.add(subscribeMessage)
                _message = NetworkResults.NotYet()
                val mediaPlayer = MediaPlayer.create(context, R.raw.pop)
                mediaPlayer.start()
            }
            return
        }
        viewModelScope.launch {
            _message = NetworkResults.Loading()
            val body = MultipartBody.Part.createFormData("question", message.text)
            val response = repo.remote.sendMessage(body)
            _message = HandleResponse(response).handleResult()
            if (_message is NetworkResults.Success) {

                var animeBotAnswer =
                    Message(_message.data!!.text.replace("\n", ""), "AnimeBot", "10:00")
                animeBotAnswer.text = animeBotAnswer.text.substringAfter(":")
                animeBotAnswer.timestamp = "$current"
                messages.add(animeBotAnswer)
                _message = NetworkResults.NotYet()
                val mediaPlayer = MediaPlayer.create(context, R.raw.pop)
                mediaPlayer.start()

            } else if (_message is NetworkResults.Error) {
                val animeBotAnswer =
                    Message("offline", "AnimeBot", current)
                messages.add(animeBotAnswer)
                _message = NetworkResults.NotYet()
            }
            //Log.d("message_debug", _message.data!!.text)
        }

    }


}