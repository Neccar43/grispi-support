package com.novacodestudios.grispisupport.presentation.filteredtickets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.novacodestudios.grispisupport.domain.repository.UserRepository
import com.novacodestudios.grispisupport.presentation.filteredtickets.FilteredTicketsEvent.Clicked
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import com.novacodestudios.grispisupport.presentation.profile.UserTicketFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilteredTicketsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
) : ViewModel() {
    var state by mutableStateOf(FilteredTicketsState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val params = savedStateHandle.toRoute<Screen.FilteredTickets>()
            val userId = params.userId
            val filter = params.filter
            state = state.copy(filter = filter)

            state = when (filter) {
                UserTicketFilter.ASSIGNEE -> state.copy(
                    tickets = userRepository.getAssignedTickets(
                        userId
                    )
                )

                UserTicketFilter.REQUEST -> state.copy(
                    tickets = userRepository.getRequestedTickets(
                        userId
                    )
                )

                UserTicketFilter.FOLLOW -> state.copy(
                    tickets = userRepository.getFollowingTickets(
                        userId
                    )
                )

                UserTicketFilter.MENTION -> state.copy(
                    tickets = userRepository.getMentionedTickets(
                        userId
                    )
                )
            }
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
    val filter: UserTicketFilter = UserTicketFilter.ASSIGNEE,
    val tickets: List<Ticket> = emptyList(),
)

sealed interface FilteredTicketsEvent {
    data object Clicked : FilteredTicketsEvent
}