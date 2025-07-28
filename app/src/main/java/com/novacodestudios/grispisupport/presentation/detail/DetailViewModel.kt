package com.novacodestudios.grispisupport.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import com.novacodestudios.grispisupport.presentation.util.dummyMessageList
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(DetailState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val id = savedStateHandle.toRoute<Screen.Detail>().id
        val ticket = dummyTicketList.find { it.id == id }
        val messages= dummyMessageList.filter { it.ticketId==id }
        state = state.copy(ticket = ticket, messageList = messages.sortedBy { it.sentAt })

    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnActiveTabChange -> state =
                state.copy(isConversation = event.isConversation)

            is DetailEvent.OnReplyTextChange -> state = state.copy(replyText = event.text)
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class DetailState(
    val isLoading: Boolean = false,
    val ticket: Ticket? = null,
    val isConversation: Boolean = true,
    val replyText: String="",
    val messageList: List<Message> = emptyList()
)

sealed interface DetailEvent {
    data class OnActiveTabChange(val isConversation: Boolean): DetailEvent
    data class OnReplyTextChange(val text: String): DetailEvent
}