package com.novacodestudios.grispisupport.presentation.model

sealed interface ConversationItem {
    data class DateHeader(val label: String) : ConversationItem
    data class MessageItem(val message: Message) : ConversationItem
}