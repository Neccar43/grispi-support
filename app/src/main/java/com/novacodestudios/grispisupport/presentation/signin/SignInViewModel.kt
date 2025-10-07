package com.novacodestudios.grispisupport.presentation.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novacodestudios.grispisupport.domain.repository.AuthRepository
import com.novacodestudios.grispisupport.presentation.util.DummyDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
            SignInEvent.OnBackToDomainClick -> state = state.copy(isDomainValid = false, domainError = null)
        }
    }

    private fun next() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, domainError = null)

            if (!state.isDomainValid) {
                val valid = authRepository.validateDomain(state.domain)
                if (valid) {
                    state = state.copy(isDomainValid = true, isLoading = false)
                } else {
                    state = state.copy(
                        domainError = "Domain kayıtlı değil",
                        isLoading = false
                    )
                }
                return@launch
            }

            val success = authRepository.signIn(
                state.domain,
                state.email,
                state.password
            )
            state = state.copy(isLoading = false)
            if (success) {
                _eventFlow.emit(UIEvent.NavigateList)
            } else {
                _eventFlow.emit(UIEvent.ShowSnackBar("Email veya parola hatalı"))
            }
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
        data object NavigateList : UIEvent
    }
}


data class SignInState(
    val isLoading: Boolean = false,
    val domain: String = "test",
    val domainError: String? = null,
    val isDomainValid: Boolean = false,
    val email: String = DummyDataSource.currentUser.email,
    val password: String = "test123",
)

sealed interface SignInEvent {
    data class OnDomainChange(val domain: String) : SignInEvent
    data object OnNextClick : SignInEvent
    data class OnEmailChange(val email: String) : SignInEvent
    data class OnPasswordChange(val password: String) : SignInEvent
    data object OnBackToDomainClick : SignInEvent
}