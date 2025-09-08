package com.novacodestudios.grispisupport.presentation.model


data class Ticket(
    val id: String,
    val number: Int,
    val subject: String,
    val requester: User,
    val assignee: User?,
    val followers: List<User>,
    val tags: List<Tag>, // TODO: kaldırılıp form üzerinden çekilecek
    val formId: String,
    val formResponseId: String,
    val status: TicketStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val lastMessageContent: String, // TODO: Burası message türünde olacak,
    val channel: Channel,
    val type: Type,
    val priority: Priority,
)

enum class TicketStatus {
    NEW, // sadece ilk açılışta olacak sonrasında geçiş yapılamayacak
    OPEN,
    PENDING,
    // IN_PROGRESS,
    ON_HOLD,
    RESOLVED,
    // CLOSED
}

enum class Type {
    QUESTION,
    INCIDENT,
    PROBLEM,
    TASK
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

data class Tag(val id: Int, val name: String)