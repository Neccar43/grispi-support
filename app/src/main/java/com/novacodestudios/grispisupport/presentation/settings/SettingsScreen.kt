package com.novacodestudios.grispisupport.presentation.settings

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.detail.component.DetailDialog
import com.novacodestudios.grispisupport.presentation.detail.component.DialogRadioGroup
import com.novacodestudios.grispisupport.presentation.settings.component.SettingsItem
import com.novacodestudios.grispisupport.presentation.settings.component.SettingsTopBar
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateProfile: () -> Unit,
    navigateSignIn: () -> Unit,
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is SettingsViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    SettingsScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp,
        navigateProfile = navigateProfile, navigateSignIn = navigateSignIn
    )
}

@Composable
fun SettingsScreenContent(
    state: SettingsState,
    snackbarHostState: SnackbarHostState,
    onEvent: (SettingsEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateProfile: () -> Unit,
    navigateSignIn: () -> Unit,
) {
    var isThemeDialogVisible by remember { mutableStateOf(false) }
    var isLanguageDialogVisible by remember { mutableStateOf(false) }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { SettingsTopBar(navigateUp = navigateUp) }
    ) { paddingValues ->
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HorizontalDivider()

            SettingsItem(
                onClick = { navigateProfile() },
                headlineText = "Profil",
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { }, // TODO: bildirim ayarları ekranına git veya dialog aç
                headlineText = "Bildirimler",
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { isThemeDialogVisible = true },
                headlineText = "Tema",
                supportingText = state.theme.displayName
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { isLanguageDialogVisible = true },
                headlineText = "Dil",
                supportingText = state.language.displayName
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { },
                headlineText = "Destek için yazın",
            )
            HorizontalDivider()
            SettingsItem(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://support.grispi.com".toUri())
                    context.startActivity(intent)
                },
                headlineText = "Yardım merkezi",
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { navigateSignIn() },
                headlineText = "Oturumu kapat",
            )
            HorizontalDivider()

            SettingsItem(
                onClick = { },
                headlineText = "Sürüm",
                supportingText = "v1.0.0"
            )
        }
    }

    if (isThemeDialogVisible) {
        var selectedOption by remember { mutableStateOf(state.theme) }
        DetailDialog(
            onDismiss = { isThemeDialogVisible = false },
            title = "Tema",
            text = {
                DialogRadioGroup(
                    options = ThemeOption.entries.map { it.displayName },
                    selectedOption = selectedOption.displayName,
                    onOptionSelected = { selectedOption = ThemeOption.displayNameToTheme(it) },
                )
            },
            onConfirm = {
                Log.d(TAG, "SettingsScreenContent: Selected theme: $selectedOption")
                onEvent(SettingsEvent.OnThemeSelected(selectedOption))
                isThemeDialogVisible = false
            },
        )
    }
    if (isLanguageDialogVisible) {
        var selectedOption by remember { mutableStateOf(state.language) }
        DetailDialog(
            onDismiss = { isLanguageDialogVisible = false },
            title = "Dil",
            text = {
                DialogRadioGroup(
                    options = LanguageOption.entries.map { it.displayName },
                    selectedOption = selectedOption.displayName,
                    onOptionSelected = { selectedOption = LanguageOption.displayNameToLanguage(it) },
                )
            },
            onConfirm = {
                onEvent(SettingsEvent.OnLanguageSelected(selectedOption))
                isLanguageDialogVisible = false
            },
        )
    }
}

enum class ThemeOption(val displayName: String) {
    LIGHT("Açık"),
    DARK("Koyu"),
    SYSTEM_DEFAULT("Sistem");

    companion object {
        fun displayNameToTheme(displayName: String): ThemeOption {
            return when (displayName) {
                "Açık" -> LIGHT
                "Koyu" -> DARK
                "Sistem" -> SYSTEM_DEFAULT
                else -> SYSTEM_DEFAULT
            }
        }
    }
}


enum class LanguageOption(val displayName: String) {
    TURKISH("Türkçe"),
    ENGLISH("İngilizce");

    companion object {
        fun displayNameToLanguage(displayName: String): LanguageOption {
            return when (displayName) {
                "Türkçe" -> TURKISH
                "İngilizce" -> ENGLISH
                else -> TURKISH
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    GrispiSupportTheme {
        SettingsScreenContent(
            state = SettingsState(),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            navigateUp = {},
            navigateProfile = {},
            navigateSignIn = {}
        )
    }
}

private const val TAG = "SettingsScreen"