package com.novacodestudios.grispisupport.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.novacodestudios.grispisupport.presentation.settings.SettingsEvent.Clicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            Clicked -> {}
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class SettingsState(
    val isLoading: Boolean = false,
)

sealed interface SettingsEvent {
    data object Clicked : SettingsEvent
}