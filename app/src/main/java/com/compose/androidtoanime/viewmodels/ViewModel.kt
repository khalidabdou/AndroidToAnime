package com.compose.androidtoanime.viewmodels


import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.HandleResponse
import com.compose.androidtoanime.Utils.NetworkResults
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ViewModel @Inject constructor(

    private val repo: RepositoryImpl,
    application: Application

) : AndroidViewModel(application) {

    var uploadResults: MutableLiveData<NetworkResults<String>> = MutableLiveData()

    var test: MutableLiveData<NetworkResults<String>> = MutableLiveData()


    fun upload(uri: Uri, activity: Context) = viewModelScope.launch {
        //val file = File(uri.path!!)

        //val response = service.sendPhotoFromBody(body)
        //Log.d("tbCats","url "+ uri.path!!.toString())

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver: ContentResolver = activity.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = cursor.getString(columnIndex)
            Log.d("tbCats", "path " + filePath!!.toString())
            val file = File(filePath)

            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val res = repo.remote.upload(body)
            uploadResults.value = HandleResponse(res).handleResult()
            if (uploadResults.value is NetworkResults.Success) {
                val cats =
                    uploadResults.value!!.data
                Log.d("tbCats", cats.toString())
            } else if (uploadResults.value is NetworkResults.Error) {
                Log.d("tbCats", uploadResults.value!!.message!!)
            }
        }
    }

    fun upload2() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun test(uri: Uri, context: Context) = viewModelScope.launch {

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = cursor.getString(columnIndex)
            Log.d("tbCats", "path " + filePath!!.toString())
            val file = File(filePath)

            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)


            val buffer = ByteArray(1024)
            val output = ByteArrayOutputStream()

            file.inputStream().use { input ->
                while (true) {
                    val count = input.read(buffer)
                    if (count == -1) break
                    output.write(buffer, 0, count)
                }
            }
            val bytes = output.toByteArray()
            Log.d("tbCats", "output " + bytes)
            val buffer2 = generateRequestBody(bytes)
            Log.d("tbCats", "buffer2 " + buffer2.toString())
            //return@launch
            val res = repo.remote.test(buffer2.toString())
            Log.d("tbCats", "res " + res.toString())

            return@launch

            //uploadResults.value = HandleResponse().handleResult()

            if (uploadResults.value is NetworkResults.Success) {
                val cats =
                    uploadResults.value!!.data
                Log.d("tbCats", cats.toString())
            } else if (uploadResults.value is NetworkResults.Error) {
                Log.d("tbCats", uploadResults.value!!.message!!)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateRequestBody(image: ByteArray): Map<String, Any> {

        val busiId = "different_dimension_me_img_entry"
        val extraDict = mapOf("version" to 2)
        val gson = Gson()
        val encodedImage = Base64.getEncoder().encodeToString(image)

        return mapOf(
            "busiId" to busiId,
            "extra" to gson.toJson(extraDict),
            "images" to listOf(encodedImage)
        )
    }

}