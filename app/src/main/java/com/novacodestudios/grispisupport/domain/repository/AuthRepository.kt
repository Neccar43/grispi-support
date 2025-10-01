package com.novacodestudios.grispisupport.domain.repository

interface AuthRepository {
    suspend fun validateDomain(domain: String): Boolean
    suspend fun signIn(domain: String, email: String, password: String): Boolean
    suspend fun signOut()
    suspend fun getCurrentUserId(): String?
}