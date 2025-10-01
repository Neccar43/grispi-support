package com.novacodestudios.grispisupport.domain.repository

import com.novacodestudios.grispisupport.presentation.model.Message

interface MessageRepository {
    suspend fun getMessages(ticketId: String): List<Message>
    suspend fun sendMessage(ticketId: String, message: Message): Message
}