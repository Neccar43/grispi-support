package com.novacodestudios.grispisupport.presentation.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.novacodestudios.grispisupport.presentation.feedback.FeedbackEvent.Clicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(FeedbackState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: FeedbackEvent) {
        when (event) {
            Clicked -> {}
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class FeedbackState(
    val isLoading: Boolean = false,
)

sealed interface FeedbackEvent {
    data object Clicked : FeedbackEvent
}