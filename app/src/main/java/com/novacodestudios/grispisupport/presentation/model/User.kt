package com.novacodestudios.grispisupport.presentation.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val organization: String? = null,
    val groups: List<String> = emptyList(),
    val phone: String? = null,
)

enum class UserRole {
    END_USER,
    AGENT,
}
