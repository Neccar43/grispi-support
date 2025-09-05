package com.novacodestudios.grispisupport.di

import android.content.Context
import com.novacodestudios.grispisupport.data.local.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun providePreferences(@ApplicationContext context: Context): Preferences = Preferences(context)
}