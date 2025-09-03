package com.novacodestudios.grispisupport.presentation.model

data class TicketHistory(
    val id: String,
    val ticketId: String,
    val createdAt: Long,
    val authorId: String,
    val events: List<TicketEvent>
)


sealed class TicketEvent {
    data class Comment(val body: String) : TicketEvent()
    data class FieldChange(val fieldName: String, val from: String?, val to: String?) :
        TicketEvent()
}