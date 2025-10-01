package com.novacodestudios.grispisupport.data.repository

import com.novacodestudios.grispisupport.domain.repository.TicketRepository
import com.novacodestudios.grispisupport.presentation.macro.Macro
import com.novacodestudios.grispisupport.presentation.model.Tag
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketHistory
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeTicketRepository @Inject constructor() : TicketRepository {

    private val tickets = DummyDataSource.ticketList.toMutableList()

    private val histories = DummyDataSource.histories

    private val tags = tickets.flatMap { it.tags }.distinct()
    private val macros = DummyDataSource.macros

    override suspend fun getTickets(): List<Ticket> {
        delay(500)
        return tickets
    }

    override suspend fun searchTickets(query: String): List<Ticket> {
        delay(200)
        return tickets.filter { it.subject.contains(query, ignoreCase = true) }
    }

    override suspend fun getTicketById(id: String): Ticket? {
        delay(200)
        return tickets.find { it.id == id }
    }

    override suspend fun updateTicket(ticket: Ticket): Boolean {
        delay(300)
        return tickets.indexOfFirst { it.id == ticket.id }
            .takeIf { it != -1 }
            ?.let {
                tickets[it] = ticket
                true
            } ?: false
    }

    override suspend fun getHistories(ticketId: String): List<TicketHistory> {
        delay(300)
        return histories.filter { it.ticketId == ticketId }.sortedByDescending { it.createdAt }
    }

    override suspend fun getTags(ticketId: String): List<Tag> {
        delay(200)
        return tags
    }

    override suspend fun getMacro(macroId: String): Macro? {
        delay(200)
        return macros.find { it.id == macroId }
    }

    override suspend fun getMacros(): List<Macro> {
        delay(300)
        return macros
    }
}