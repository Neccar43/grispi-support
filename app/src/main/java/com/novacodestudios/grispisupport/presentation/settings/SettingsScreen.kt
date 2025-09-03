package com.novacodestudios.grispisupport.presentation.settings

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
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
                //supportingText = "Çevrimiçi"
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { }, // TODO: bildirim ayarları ekranına git veya dialog aç
                headlineText = "Bildirimler",
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { },
                headlineText = "Tema",
                supportingText = "Açık" // TODO: tema seçimi ekle (Açık, Koyu, Sistem
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { },
                headlineText = "Dil",
                supportingText = "Türkçe" // TODO: dil seçimi ekle
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