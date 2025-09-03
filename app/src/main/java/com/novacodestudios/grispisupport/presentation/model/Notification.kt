package com.novacodestudios.grispisupport.presentation.model

data class Notification(
    val id: String,
    val isRead: Boolean,
    val timestamp: Long,
    val user: User,
    val ticket: Ticket,
)