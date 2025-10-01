package com.novacodestudios.grispisupport.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.novacodestudios.grispisupport.domain.repository.AuthRepository
import com.novacodestudios.grispisupport.domain.repository.UserRepository
import com.novacodestudios.grispisupport.presentation.model.User
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUserId() ?: return@launch
            val userId = savedStateHandle.toRoute<Screen.Profile>().id
            val user = userRepository.getUser(userId)
            val assignedTickets = userRepository.getAssignedTickets(userId)
            val requestedTickets = userRepository.getRequestedTickets(userId)
            val mentionedTickets = userRepository.getMentionedTickets(userId)
            val followedTickets = userRepository.getFollowingTickets(userId)

            state = state.copy(
                user = user, isCurrentUser = userId == currentUser,
                assignedTicketCount = assignedTickets.size,
                requestedTicketCount = requestedTickets.size,
                mentionedTicketCount = mentionedTickets.size,
                followedTicketCount = followedTickets.size,
                name = user?.name.orEmpty()
            )
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnConfirmName -> {
            }

            is ProfileEvent.OnNameChange -> state = state.copy(name = event.value)
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class ProfileState(
    val isLoading: Boolean = false,
    val isCurrentUser: Boolean = false,
    val user: User? = null,
    val name: String = user?.name.orEmpty(),
    val assignedTicketCount: Int = 0,
    val requestedTicketCount: Int = 0,
    val followedTicketCount: Int = 0,
    val mentionedTicketCount: Int = 0,
)

sealed interface ProfileEvent {
    data class OnNameChange(val value: String) : ProfileEvent
    data object OnConfirmName : ProfileEvent
}