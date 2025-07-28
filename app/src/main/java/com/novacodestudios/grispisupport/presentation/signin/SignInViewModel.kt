package com.novacodestudios.grispisupport.presentation.signin

import android.util.Log
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
class SignInViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(SignInState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnDomainChange -> state = state.copy(domain = event.domain)
            SignInEvent.OnNextClick -> next()
        }
    }

    fun next(){
        viewModelScope.launch {
            Log.d(TAG, "next: domain: ${state.domain}")
            state = state.copy(domainError = null)
            val isRegistered= "test"==state.domain
            if (isRegistered){
                _eventFlow.emit(UIEvent.NavigateList)
                return@launch
            }

            state = state.copy(isLoading = false, domainError = "Domain kayıtlı değil")
        }
    }
    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
        data object NavigateList: UIEvent
    }
    companion object{
        private const val TAG = "SignInViewModel"
    }
}

data class SignInState(
    val isLoading: Boolean = false,
    val domain: String="",
    val domainError: String?=null,
    )

sealed interface SignInEvent {
    data class OnDomainChange(val domain: String) : SignInEvent
    data object OnNextClick: SignInEvent
}