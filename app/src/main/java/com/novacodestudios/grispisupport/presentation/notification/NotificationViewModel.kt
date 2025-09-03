package com.novacodestudios.grispisupport.presentation.notification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.novacodestudios.grispisupport.presentation.model.Notification
import com.novacodestudios.grispisupport.presentation.util.dummyNotifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(NotificationState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        state = state.copy(
            notifications = dummyNotifications
        )
    }


    fun onEvent(event: NotificationEvent) {
        when (event) {
            NotificationEvent.MarkAllAsRead -> {
                state = state.copy(
                    notifications = state.notifications.map {
                        it.copy(isRead = true)
                    }
                )
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

