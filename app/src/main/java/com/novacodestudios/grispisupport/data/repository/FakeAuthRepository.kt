package com.novacodestudios.grispisupport.data.repository

import com.novacodestudios.grispisupport.domain.repository.AuthRepository
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    override suspend fun validateDomain(domain: String): Boolean {
        delay(500)
        return domain == "test"
    }

    override suspend fun signIn(domain: String, email: String, password: String): Boolean {
        delay(500)
        return domain == "test" && email == DummyDataSource.currentUser.email && password == "test123"
    }

    override suspend fun signOut() {
        delay(500)
    }

    override suspend fun getCurrentUserId(): String? {
        delay(500)
        return DummyDataSource.currentUser.id
    }
}