package com.novacodestudios.grispisupport.domain.repository

import com.novacodestudios.grispisupport.presentation.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(userId: String): List<Notification>
    suspend fun markAllAsRead(userId: String): List<Notification>
}