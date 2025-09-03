package com.novacodestudios.grispisupport.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(ListState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            delay(500)

            state = state.copy(isLoading = false, tickets = dummyTicketList)

        }
    }

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.OnQueryChanged -> state = state.copy(query = event.query)
            ListEvent.OnSearchClicked -> search(state.query)
        }
    }

    private fun search(query: String?) {
        if (query.isNullOrEmpty()) {
            state = state.copy(searchTickets = emptyList())
            return
        }
        val searchResult = state.tickets.filter { it.subject.contains(query, ignoreCase = true) }
        state = state.copy(searchTickets = searchResult)
    }


    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class ListState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val sortOptions: SortOptions = SortOptions.DEFAULT,
    val isAscending: Boolean = false,
    val query: String? = null,
    val searchTickets: List<Ticket> = emptyList(), // TODO: ileride sadece ticket değil organizasyon ve kullanıcı da içerebilir
)

sealed interface ListEvent {
    data class OnQueryChanged(val query: String?) : ListEvent
    data object OnSearchClicked : ListEvent
}

