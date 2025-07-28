package com.novacodestudios.grispisupport.presentation.model

data class Ticket(
    val id: String,
    val number: Int,
    val subject: String,
    val requester: User,
    val assignee: User?,
    val followers: List<User>,
    val tags: List<String>,
    val form: String?,
    val status: TicketStatus,
    val createdAt: Long,
    val updatedAt: Long
)

enum class TicketStatus {
    OPEN,
    PENDING,       // kullanıcıdan cevap bekleniyor
    IN_PROGRESS,   // agent ilgileniyor
    ON_HOLD,       // başka bir birimden cevap bekleniyor
    RESOLVED,      // çözüm sağlandı
    CLOSED         // tamamen kapatıldı
}