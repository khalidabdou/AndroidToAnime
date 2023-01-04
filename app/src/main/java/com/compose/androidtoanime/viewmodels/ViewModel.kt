package com.compose.androidtoanime.viewmodels


import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.HandleResponse
import com.compose.androidtoanime.Utils.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(

    private val repo: RepositoryImpl,
    application: Application

) : AndroidViewModel(application) {

    var uploadResults: MutableLiveData<NetworkResults<String>> = MutableLiveData()

    var test: MutableLiveData<NetworkResults<String>> = MutableLiveData()


    fun upload(uri: Uri) = viewModelScope.launch {
        val file = File(uri.path)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("image", file.name, requestBody)



        Log.d("tbCats", uri.path.toString())
        //return@launch
        val res = repo.remote.upload(image)
        uploadResults.value = HandleResponse(res).handleResult()
        if (uploadResults.value is NetworkResults.Success) {
            val cats =
                uploadResults.value!!.data
            Log.d("tbCats", cats.toString())
        } else if (uploadResults.value is NetworkResults.Error) {
            Log.d("tbCats", uploadResults.value!!.message!!)
        }
    }


    fun test() = viewModelScope.launch {
        val res = repo.remote.test()
        test.value = HandleResponse(res).handleResult()
        if (test.value is NetworkResults.Success) {
            val cats =
                test.value!!.data
            Log.d("tbCats", cats.toString())
        } else if (test.value is NetworkResults.Error) {
            Log.d("tbCats", test.value!!.message!!)
        }
    }

}