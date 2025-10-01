package com.novacodestudios.grispisupport.di

import android.content.Context
import com.novacodestudios.grispisupport.data.local.Preferences
import com.novacodestudios.grispisupport.data.repository.FakeAuthRepository
import com.novacodestudios.grispisupport.data.repository.FakeFormRepository
import com.novacodestudios.grispisupport.data.repository.FakeMessageRepository
import com.novacodestudios.grispisupport.data.repository.FakeNotificationRepository
import com.novacodestudios.grispisupport.data.repository.FakeTicketRepository
import com.novacodestudios.grispisupport.data.repository.FakeUserRepository
import com.novacodestudios.grispisupport.domain.repository.AuthRepository
import com.novacodestudios.grispisupport.domain.repository.FormRepository
import com.novacodestudios.grispisupport.domain.repository.MessageRepository
import com.novacodestudios.grispisupport.domain.repository.NotificationRepository
import com.novacodestudios.grispisupport.domain.repository.TicketRepository
import com.novacodestudios.grispisupport.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    fun providePreferences(@ApplicationContext context: Context): Preferences = Preferences(context)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        fakeAuthRepository: FakeAuthRepository
    ): AuthRepository

    @Binds
    abstract fun bindTicketRepository(
        fakeTicketRepository: FakeTicketRepository
    ): TicketRepository

    @Binds
    abstract fun bindFormRepository(
        fakeFormRepository: FakeFormRepository
    ): FormRepository

    @Binds
    abstract fun bindMessageRepository(
        fakeMessageRepository: FakeMessageRepository
    ): MessageRepository

    @Binds
    abstract fun bindUserRepository(
        fakeUserRepository: FakeUserRepository
    ): UserRepository

    @Binds
    abstract fun bindNotificationRepository(
        fakeNotificationRepository: FakeNotificationRepository
    ): NotificationRepository

}