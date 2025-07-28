package com.novacodestudios.grispisupport.presentation.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)

enum class UserRole {
    END_USER,
    AGENT,
}