package com.novacodestudios.grispisupport.presentation.model

data class Message(
    val id: String,
    val ticketId: String,
    val senderId: String,
    val content: String,
    val sentAt: Long,
    val attachments: List<Attachment> = emptyList()
)

data class Attachment(
    val id: String,
    val type: AttachmentType,
    val url: String,          // dosyanın erişim linki
    val name: String? = null, // dosya adı (opsiyonel)
    val size: Long? = null    // byte cinsinden boyut (opsiyonel)
)

enum class AttachmentType {
    IMAGE, FILE,
}