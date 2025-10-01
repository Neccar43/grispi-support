package com.novacodestudios.grispisupport.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class Preferences(private val context: Context) {
    private val dataStore = context.dataStore

    suspend fun <T> getData(key: Preferences.Key<T>): T? = dataStore.getData(key)

    fun <T> observeData(key: Preferences.Key<T>): Flow<T?> = dataStore.observeData(key)

    suspend fun <T> editData(key: Preferences.Key<T>, value: T) = dataStore.editData(key, value)


}


suspend fun <T> DataStore<Preferences>.getData(key: Preferences.Key<T>): T? = data.map {
    it[key]
}.firstOrNull()

fun <T> DataStore<Preferences>.observeData(key: Preferences.Key<T>): Flow<T?> = data.map {
    it[key]
}

suspend fun <T> DataStore<Preferences>.editData(key: Preferences.Key<T>, value: T) = edit {
    it[key] = value
}

object Keys {
    val THEME = stringPreferencesKey("theme")
    val LANGUAGE = stringPreferencesKey("language")
}