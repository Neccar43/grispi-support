package com.novacodestudios.grispisupport.data.repository

import com.novacodestudios.grispisupport.domain.repository.MessageRepository
import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import javax.inject.Inject

class FakeMessageRepository @Inject constructor() : MessageRepository {

    private var messages = DummyDataSource.messageList

    override suspend fun getMessages(ticketId: String): List<Message> {
        return messages.filter { it.ticketId == ticketId }.sortedBy { it.sentAt }
    }

    override suspend fun sendMessage(
        ticketId: String,
        message: Message
    ): Message {
        messages += message
        return message
    }
}