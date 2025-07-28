package com.novacodestudios.grispisupport.presentation.model

data class Message(
    val id: String,
    val ticketId: String,
    val senderId: String,
    val content: String,
    val sentAt: Long,
)
