package com.compose.androidtoanime.Utils

sealed class NetworkResults<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResults<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResults<T>(data, message)
    class Loading<T> : NetworkResults<T>()
    class Cached<T> : NetworkResults<T>()

}