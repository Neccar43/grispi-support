package com.novacodestudios.grispisupport.presentation.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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
            FeedbackEvent.OnSendClick -> {
                if (state.feedbackText.isBlank()) return

                state = state.copy(feedbackText = "", selectedStarCount = 0)
                viewModelScope.launch {
                    _eventFlow.emit(UIEvent.ShowSnackBar("Geri bildirimiz gönderildi, teşekkürler!"))
                }
            }
            is FeedbackEvent.OnFeedbackChange -> {
                state = state.copy(feedbackText = event.text)
            }
            is FeedbackEvent.OnStarSelected -> {
                state = state.copy(selectedStarCount = event.starCount)
            }
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class FeedbackState(
    val isLoading: Boolean = false,
    val feedbackText: String = "",
    val selectedStarCount: Int = 0,
)

sealed interface FeedbackEvent {
    data object OnSendClick : FeedbackEvent
    data class OnFeedbackChange(val text: String) : FeedbackEvent
    data class OnStarSelected(val starCount: Int) : FeedbackEvent
}