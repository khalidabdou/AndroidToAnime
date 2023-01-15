package com.compose.androidtoanime.preferences.implimentation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.billingclient.api.Purchase
import com.google.gson.Gson
import com.wishes.jetpackcompose.preferences.abstraction.DataRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject


private const val PREFERENCES_NAME = "my_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DataRepository {


    override suspend fun savePurchase(key: String, value: Purchase) {
        val gson = Gson()
        val purchase = gson.toJson(value)
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = purchase
        }

    }

    override suspend fun getPurchase(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

}

