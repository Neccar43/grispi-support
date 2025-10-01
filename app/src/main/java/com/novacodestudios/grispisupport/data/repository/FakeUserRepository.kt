package com.novacodestudios.grispisupport.data.repository

import com.novacodestudios.grispisupport.domain.repository.UserRepository
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.User
import com.novacodestudios.grispisupport.presentation.model.UserRole
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    private val users = DummyDataSource.allUsers
    private val tickets = DummyDataSource.ticketList
    override suspend fun getUsers(): List<User> {
        return users
    }

    override suspend fun getUser(userId: String): User? {
        return users.find { it.id == userId }
    }

    override suspend fun searchUsers(query: String): List<User> {
        return users.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.email.contains(query, ignoreCase = true) ||
                    (it.phone?.contains(query, ignoreCase = true) ?: false)
        }
    }

    override suspend fun getAgentUsers(): List<User> {
        return users.filter { it.role == UserRole.AGENT }
    }

    override suspend fun getAssignedTickets(userId: String): List<Ticket> {
        return tickets.filter { it.assignee?.id == userId }
    }

    override suspend fun getRequestedTickets(userId: String): List<Ticket> {
        return tickets.filter { it.requester.id == userId }
    }

    override suspend fun getMentionedTickets(userId: String): List<Ticket> {
        return emptyList()
    }

    override suspend fun getFollowingTickets(userId: String): List<Ticket> {
        return tickets.filter { ticket ->
            ticket.followers.any { it.id == userId }
        }
    }
}