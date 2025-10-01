package com.novacodestudios.grispisupport.presentation.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novacodestudios.grispisupport.data.local.Keys
import com.novacodestudios.grispisupport.data.local.Preferences
import com.novacodestudios.grispisupport.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: Preferences,
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            preferences.observeData(Keys.LANGUAGE).collectLatest { language ->
                Log.d(TAG, "init: language: $language")
                language?.let {
                    state = state.copy(language = LanguageOption.valueOf(it))
                }
            }

        }

        viewModelScope.launch {
            preferences.observeData(Keys.THEME).collectLatest { theme ->
                Log.d(TAG, "init: theme: $theme")
                theme?.let {
                    state = state.copy(theme = ThemeOption.valueOf(it))
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnLanguageSelected -> {
                viewModelScope.launch {
                    preferences.editData(Keys.LANGUAGE, event.language.name)
                }
            }

            is SettingsEvent.OnThemeSelected -> {
                viewModelScope.launch {
                    preferences.editData(Keys.THEME, event.theme.name)
                }
            }
        }
    }

    sealed interface UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent
    }
}

data class SettingsState(
    val isLoading: Boolean = false,
    val theme: ThemeOption = ThemeOption.SYSTEM_DEFAULT,
    val language: LanguageOption = LanguageOption.TURKISH
)

sealed interface SettingsEvent {
    data class OnThemeSelected(val theme: ThemeOption) : SettingsEvent
    data class OnLanguageSelected(val language: LanguageOption) : SettingsEvent
}

private const val TAG = "SettingsViewModel"