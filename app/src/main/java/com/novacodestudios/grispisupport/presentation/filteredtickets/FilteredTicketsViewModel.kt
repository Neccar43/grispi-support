package com.novacodestudios.grispisupport.presentation.filteredtickets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import com.novacodestudios.grispisupport.presentation.filteredtickets.FilteredTicketsEvent.Clicked
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import com.novacodestudios.grispisupport.presentation.profile.UserTicketFilter
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import kotlin.collections.count

@HiltViewModel
class FilteredTicketsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(FilteredTicketsState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    init {
        val params=savedStateHandle.toRoute<Screen.FilteredTickets>()
        val userId= params.userId
        val filter= params.filter

        state = when (filter) {
            UserTicketFilter.ASSIGNEE -> state.copy(title = "Atandı", tickets = dummyTicketList.filter { it.assignee?.id==userId })
            UserTicketFilter.REQUEST -> state.copy(title = "Talep ettiği", tickets = dummyTicketList.filter { it.requester.id==userId })
            UserTicketFilter.FOLLOW -> state.copy(title = "Takip Ettiği", tickets = dummyTicketList.filter { it.followers.any { f-> f.id==userId } })
            UserTicketFilter.MENTION -> state.copy(title = "Bilgilendirilenler", tickets = emptyList())
        }
    }

    fun onEvent(event: FilteredTicketsEvent) {
        when (event) {
            Clicked -> {}
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class FilteredTicketsState(
    val isLoading: Boolean = false,
    val title:String = "",
    val tickets: List<Ticket> = emptyList(),
)

sealed interface FilteredTicketsEvent {
    data object Clicked : FilteredTicketsEvent
}