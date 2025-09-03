package com.novacodestudios.grispisupport.presentation.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novacodestudios.grispisupport.presentation.util.currentUser
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
            is SignInEvent.OnEmailChange -> state = state.copy(email = event.email)
            is SignInEvent.OnPasswordChange -> state = state.copy(password = event.password)
        }
    }

    fun next() {
        viewModelScope.launch {
            Log.d(TAG, "next: domain: ${state.domain}")

            if (state.isDomainValid) {
                val hasError = listOf(
                    state.email != currentUser.email,
                    state.password != "test123",
                    state.email.isBlank(),
                    state.password.isBlank(),
                ).any { it }

                if (hasError) {
                    _eventFlow.emit(UIEvent.ShowSnackBar("Email veya parola hatalı"))
                    return@launch
                }
                _eventFlow.emit(UIEvent.NavigateList)
                return@launch
            }

            state = state.copy(domainError = null)
            val isRegistered = "test" == state.domain
            if (isRegistered) {
                //_eventFlow.emit(UIEvent.NavigateList)
                state = state.copy(isDomainValid = true)
            }

            state = state.copy(isLoading = false, domainError = "Domain kayıtlı değil")
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
        data object NavigateList : UIEvent
    }

    companion object {
        private const val TAG = "SignInViewModel"
    }
}

data class SignInState(
    val isLoading: Boolean = false,
    val domain: String = "test",
    val domainError: String? = null,
    val isDomainValid: Boolean = false,
    val email: String = currentUser.email,
    val password: String = "test123",
)

sealed interface SignInEvent {
    data class OnDomainChange(val domain: String) : SignInEvent
    data object OnNextClick : SignInEvent
    data class OnEmailChange(val email: String) : SignInEvent
    data class OnPasswordChange(val password: String) : SignInEvent
}