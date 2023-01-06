package com.compose.androidtoanime.viewmodels


import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.compose.androidtoanime.BuildConfig
import com.compose.androidtoanime.RepositoryImpl
import com.compose.androidtoanime.Utils.HandleResponse
import com.compose.androidtoanime.Utils.NetworkResults
import com.compose.androidtoanime.data.ResponsePhoto
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
import kotlin.math.log


@HiltViewModel
class ViewModel @Inject constructor(

    private val repo: RepositoryImpl,
    application: Application

) : AndroidViewModel(application) {
    val TAG_D="debug_response"
    var uploadResults: MutableLiveData<NetworkResults<ResponsePhoto>> = MutableLiveData(NetworkResults.Loading())
    var readyImage by mutableStateOf<NetworkResults<ResponsePhoto>?>(null)



    var test: MutableLiveData<NetworkResults<String>> = MutableLiveData()


    @SuppressLint("Recycle")
    fun upload(uri: Uri, activity: Context) = viewModelScope.launch {
        //val file = File(uri.path!!)

        Log.d(TAG_D, "path ${uri}  ${uri.path}" )
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver: ContentResolver = activity.contentResolver
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = cursor.getString(columnIndex)
            Log.d(TAG_D, "path ${filePath} +${columnIndex}" )


            val file = File(uri.path!!)

            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val res = repo.remote.upload(body)
            readyImage = HandleResponse(res).handleResult()
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } finally {
            cursor?.close()
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