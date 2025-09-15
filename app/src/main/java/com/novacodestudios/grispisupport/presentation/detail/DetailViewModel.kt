package com.novacodestudios.grispisupport.presentation.detail

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.novacodestudios.grispisupport.presentation.detail.component.FieldResponse
import com.novacodestudios.grispisupport.presentation.detail.component.Form
import com.novacodestudios.grispisupport.presentation.detail.component.FormResponse
import com.novacodestudios.grispisupport.presentation.model.Attachment
import com.novacodestudios.grispisupport.presentation.model.Channel
import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.model.Tag
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketHistory
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.model.User
import com.novacodestudios.grispisupport.presentation.model.UserRole
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import com.novacodestudios.grispisupport.presentation.util.allDummyUsers
import com.novacodestudios.grispisupport.presentation.util.currentUser
import com.novacodestudios.grispisupport.presentation.util.dummyFormResponses
import com.novacodestudios.grispisupport.presentation.util.dummyForms
import com.novacodestudios.grispisupport.presentation.util.dummyHistories
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
        val messages = dummyMessageList.filter { it.ticketId == id }
        state = state.copy(ticket = ticket, messageList = messages.sortedBy { it.sentAt }, oldTicket = ticket)
        val histories =
            dummyHistories.filter { it.ticketId == id }.sortedByDescending { it.createdAt }

        val selectedForm = dummyForms.find { it.id == ticket?.formId }
        val formResponse = dummyFormResponses.find { it.id == ticket?.formResponseId }

        val agents = allDummyUsers.filter { user -> user.role== UserRole.AGENT }

        state = state.copy(
            ticketHistories = histories,
            forms = dummyForms,
            selectedForm = selectedForm,
            formResponse = formResponse,
            agentUser = agents
        )


    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnActiveTabChange -> state =
                state.copy(activeTab = event.tab)

            is DetailEvent.OnReplyTextChange -> state = state.copy(replyText = event.text)

            is DetailEvent.OnUserQueryChange -> {
                state = state.copy(userQuery = event.query)
                searchUsers(event.query)
            }

            is DetailEvent.OnTagQueryChange -> {
                state = state.copy(tagQuery = event.query)
                searchTags(event.query)
            }

            is DetailEvent.OnFormChange -> {
                state = state.copy(selectedForm = event.form, formResponse = event.form.toResponse(state.ticket!!.id))
            }

            is DetailEvent.OnFieldResponseChange -> {
                Log.d(TAG, "OnFieldResponseChange: ${event.fieldId} ${event.value}")
                state.formResponse?.let {
                    changeResponse(it, event.fieldId, event.value)
                } ?:run {
                    Log.d(TAG, "OnFieldResponseChange: form response not found")
                    val formResponse = state.selectedForm?.toResponse(state.ticket!!.id)
                    state = state.copy(formResponse = formResponse)
                    formResponse?.let { changeResponse(it, event.fieldId, event.value)}
                }
            }

            is DetailEvent.OnStatusChange -> state = state.copy(
                ticket = state.ticket?.copy(
                    status = event.status,
                )
            )
            is DetailEvent.OnChannelChange -> state = state.copy(
                selectedChannel = event.channel
            )

            is DetailEvent.OnSendReply -> {
                if (state.replyText.isBlank()) {
                    return
                }
                val newMessage = Message(
                    id = "m_${System.currentTimeMillis()}",
                    ticketId = state.ticket!!.id,
                    senderId = currentUser.id, // TODO: giriş yapan kullanıcı olacak
                    content = state.replyText,
                    sentAt = System.currentTimeMillis(),
                    attachments = event.attachment
                )
                state = state.copy(
                    messageList = state.messageList + newMessage,
                    replyText = "",
                    ticket = state.ticket?.copy(
                        lastMessageContent = newMessage.content,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }

        }
    }

    private fun changeResponse(formResponse: FormResponse, fieldId: String, value: List<String>) {
        Log.d(TAG, "OnFieldResponseChange: found form response ${formResponse.id}")
        val newFieldResponse =
            formResponse.responses.first { it.fieldId == fieldId }
                .copy(value = value)
        state =
            state.copy(formResponse = formResponse.copy(responses = formResponse.responses.map { if (it.fieldId == fieldId) newFieldResponse else it }))

    }

    private fun searchUsers(query: String) {
        if (query.isBlank() || query.isEmpty()) {
            state = state.copy(searchUsers = emptyList())
            return
        }
        allDummyUsers.filter {
            it.name.contains(query, ignoreCase = true) || it.email.contains(
                query,
                ignoreCase = true
            )
        }.let {
            state = state.copy(searchUsers = it)
        }

    }

    private fun searchTags(query: String) {
        if (query.isBlank() || query.isEmpty()) {
            state = state.copy(searchTags = emptyList())
            return
        }
        val allTags = dummyTicketList.flatMap { it.tags }.distinct()
        allTags.filter {
            it.name.contains(query, ignoreCase = true)
        }.let {
            state = state.copy(searchTags = it)
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class DetailState(
    val isLoading: Boolean = false,
    val oldTicket: Ticket? = null,
    val ticket: Ticket? = null,
    val activeTab: DetailTabs = DetailTabs.Conversation,
    val replyText: String = "",
    val messageList: List<Message> = emptyList(),
    val searchUsers: List<User> = emptyList(),
    val searchTags: List<Tag> = emptyList(),
    val userQuery: String = "",
    val tagQuery: String = "",
    val ticketHistories: List<TicketHistory> = emptyList(),
    val forms: List<Form> = emptyList(),
    val selectedForm: Form? = null,
    val formResponse: FormResponse? = null,
    val selectedChannel : Channel = Channel.PUBLIC_RESPONSE,
    val agentUser:List<User> = emptyList(),
){
//    val numberOfChange= ticket?.let { ticket ->
//        var count = 0
//        if(replyText.isNotBlank()) count++
//        // TODO: mention olduğunda count++ yapılacak
//        if (ticket.status != oldTicket?.status) count++
//        // TODO: form alanlarında her bir değişiklik olduğunda count++ yapılacak
//        if (ticket.subject != oldTicket?.subject) count++
//        if (ticket.requester.id != oldTicket?.requester?.id) count++
//        if (ticket.assignee?.id != oldTicket?.assignee?.id) count++
//        if (ticket.followers.map { it.id }.toSet() != oldTicket?.followers?.map { it.id }?.toSet()) count++
//        if (ticket.status != oldTicket?.status) count++
//        if (ticket.type != oldTicket?.type) count++
//        if (ticket.priority != oldTicket?.priority) count++
//        if (ticket.tags.map { it.id }.toSet() != oldTicket?.tags?.map { it.id }?.toSet()) count++
//        count
//    } ?:0

    val numberOfChange=1
}

sealed interface DetailEvent {
    data class OnActiveTabChange(val tab: DetailTabs) : DetailEvent
    data class OnReplyTextChange(val text: String) : DetailEvent
    data class OnUserQueryChange(val query: String) : DetailEvent
    data class OnTagQueryChange(val query: String) : DetailEvent
    data class OnFormChange(val form: Form) : DetailEvent
    data class OnFieldResponseChange(val fieldId: String, val value: List<String>) : DetailEvent
    data class OnStatusChange(val status: TicketStatus) : DetailEvent
    data class OnChannelChange(val channel: Channel) : DetailEvent
    data class OnSendReply(val attachment: List<Attachment>) : DetailEvent
}

private const val TAG = "DetailViewModel"

fun Form.toResponse(ticketId: String) = FormResponse(
    id = "fr_${System.currentTimeMillis()}",
    formId = this.id,
    responses = this.fields.map {
        FieldResponse(
            fieldId = it.id,
            value = emptyList()
        )
    },
    ticketId = ticketId,
)