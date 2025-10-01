package com.novacodestudios.grispisupport.domain.repository

import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUser(userId: String): User?
    suspend fun searchUsers(query: String): List<User>
    suspend fun getAgentUsers(): List<User>

    // suspend fun getUserProfile(userId: String): UserProfile
    //suspend fun getUserStats(userId: String): UserStats
    suspend fun getAssignedTickets(userId: String): List<Ticket>
    suspend fun getRequestedTickets(userId: String): List<Ticket>
    suspend fun getMentionedTickets(userId: String): List<Ticket>
    suspend fun getFollowingTickets(userId: String): List<Ticket>
}