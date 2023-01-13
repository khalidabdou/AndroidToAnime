package com.wishes.jetpackcompose.preferences.abstraction

interface DataRepository {
    suspend fun putSub(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun getSub(key: String): String?
    suspend fun getInt(key: String): Int?
}