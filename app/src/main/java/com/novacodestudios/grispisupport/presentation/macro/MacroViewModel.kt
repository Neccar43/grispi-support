package com.novacodestudios.grispisupport.presentation.macro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.novacodestudios.grispisupport.domain.repository.TicketRepository
import com.novacodestudios.grispisupport.presentation.macro.MacroEvent.Clicked
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MacroViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val ticketRepository: TicketRepository
) : ViewModel() {
    var state by mutableStateOf(
        MacroState(
            ticketId = savedStateHandle.toRoute<Screen.Macro>().ticketId
        )
    )
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val macros = ticketRepository.getMacros()
            state = state.copy(macros = macros)
        }
    }

    fun onEvent(event: MacroEvent) {
        when (event) {
            Clicked -> {}
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class MacroState(
    val isLoading: Boolean = false,
    val macros: List<Macro> = emptyList(),
    val ticketId: String,
)

sealed interface MacroEvent {
    data object Clicked : MacroEvent
}