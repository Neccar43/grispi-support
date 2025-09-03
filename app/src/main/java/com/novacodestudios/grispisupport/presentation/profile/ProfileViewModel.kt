package com.novacodestudios.grispisupport.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.novacodestudios.grispisupport.presentation.model.User
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import com.novacodestudios.grispisupport.presentation.profile.ProfileEvent.Clicked
import com.novacodestudios.grispisupport.presentation.util.allDummyUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val userId = savedStateHandle.toRoute<Screen.Profile>().id
        val user = allDummyUsers.find { it.id == userId }
        state = state.copy(user = user)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            Clicked -> {}
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
)

sealed interface ProfileEvent {
    data object Clicked : ProfileEvent
}