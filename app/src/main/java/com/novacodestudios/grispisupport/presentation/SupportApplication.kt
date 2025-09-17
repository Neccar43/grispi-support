package com.novacodestudios.grispisupport.presentation

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.novacodestudios.grispisupport.data.local.Keys
import com.novacodestudios.grispisupport.data.local.Preferences
import com.novacodestudios.grispisupport.presentation.settings.LanguageOption
import com.novacodestudios.grispisupport.presentation.settings.toLanguageCode
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class SupportApplication : Application() {
//    @Inject
//    lateinit var preferences: Preferences
//    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
//    override fun onCreate() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//            Log.d(TAG, "onCreate: Setting locale")
//            applicationScope.launch {
//                val context: Context = this@SupportApplication
//                val languageCode = preferences.getData(Keys.LANGUAGE)
//                    ?.let { LanguageOption.valueOf(it).toLanguageCode() } ?: return@launch
//                Log.d(TAG, "onCreate: Language code: $languageCode")
//                val locale = Locale(languageCode)
//                Locale.setDefault(locale)
//                val config = context.resources.configuration
//                config.setLocale(locale)
//                context.resources.updateConfiguration(config, context.resources.displayMetrics)
//                Log.d(TAG, "onCreate: Locale set to $languageCode")
//            }
//        }
//        super.onCreate()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        applicationScope.cancel()
//    }
//    companion object{
//        private const val TAG = "SupportApplication"
//    }
}