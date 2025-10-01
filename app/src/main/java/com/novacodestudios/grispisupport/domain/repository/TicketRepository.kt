package com.novacodestudios.grispisupport.domain.repository

import com.novacodestudios.grispisupport.presentation.macro.Macro
import com.novacodestudios.grispisupport.presentation.model.Tag
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketHistory

interface TicketRepository {
    suspend fun getTickets(): List<Ticket>
    suspend fun searchTickets(query: String): List<Ticket>
    suspend fun getTicketById(id: String): Ticket?
    suspend fun updateTicket(ticket: Ticket): Boolean

    // TODO: başka bir repoya taşınabilir
    suspend fun getHistories(ticketId: String): List<TicketHistory>
    suspend fun getTags(ticketId: String): List<Tag>
    suspend fun getMacro(macroId: String): Macro?
    suspend fun getMacros(): List<Macro>
}