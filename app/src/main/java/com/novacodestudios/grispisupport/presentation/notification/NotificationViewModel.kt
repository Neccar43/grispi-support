package com.novacodestudios.grispisupport.presentation.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novacodestudios.grispisupport.domain.repository.AuthRepository
import com.novacodestudios.grispisupport.domain.repository.NotificationRepository
import com.novacodestudios.grispisupport.presentation.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    var state by mutableStateOf(NotificationState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val currentUserId = authRepository.getCurrentUserId() ?: return@launch
            state = state.copy(isLoading = true)
            state = state.copy(
                notifications = notificationRepository.getNotifications(currentUserId),
                isLoading = false
            )
        }

    }


    fun onEvent(event: NotificationEvent) {
        when (event) {
            NotificationEvent.MarkAllAsRead -> {
                viewModelScope.launch {
                    val currentUserId = authRepository.getCurrentUserId() ?: return@launch
                    val notifications = notificationRepository.markAllAsRead(currentUserId)
                    state = state.copy(notifications = notifications)
                }
            }
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class NotificationState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
) {
    val unreadCount: Int = notifications.count { !it.isRead }
}

sealed interface NotificationEvent {
    data object MarkAllAsRead : NotificationEvent
}

