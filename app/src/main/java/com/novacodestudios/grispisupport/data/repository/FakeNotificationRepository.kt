package com.novacodestudios.grispisupport.data.repository

import com.novacodestudios.grispisupport.domain.repository.NotificationRepository
import com.novacodestudios.grispisupport.presentation.model.Notification
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import javax.inject.Inject

class FakeNotificationRepository @Inject constructor() : NotificationRepository {
    val notifications = DummyDataSource.notifications
    override suspend fun getNotifications(userId: String): List<Notification> {
        return notifications.filter { it.user.id == userId }
    }

    override suspend fun markAllAsRead(userId: String): List<Notification> {
        return notifications.filter { it.user.id == userId }.map { it.copy(isRead = true) }
    }
}