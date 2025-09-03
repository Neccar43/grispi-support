package com.novacodestudios.grispisupport.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.profile.component.ProfileItem
import com.novacodestudios.grispisupport.presentation.profile.component.ProfileTopBar
import com.novacodestudios.grispisupport.presentation.settings.component.SettingsItem
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.currentUser
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is ProfileViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    ProfileScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp,
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ProfileEvent) -> Unit,
    navigateUp: () -> Unit
) {
    // TODO: farklı şekilde handle et
    if (state.user == null) return
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ProfileTopBar(
                navigateUp = navigateUp,
                user = state.user
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalDivider()
            ProfileItem(
                title = "Ad soyad",
                subtitle = state.user.name,
                onClick = {} // TODO: dialog aç
            )
            ProfileItem(
                title = "E-posta",
                subtitle = state.user.email,
            )
            SettingsItem(
                headlineText = "Şifreyi sıfırla",
                onClick = {}
                //subtitle = "Masaüstü linki ile"
            )

            HorizontalDivider()
            ProfileItem(
                title = "Atandı",
                subtitle = "6"
            )
            ProfileItem(
                title = "Talep ettiği",
                subtitle = "1"
            )
            ProfileItem(
                title = "Takip ettiği",
                subtitle = "1"
            )
            ProfileItem(
                title = "Bilgilendirilenler",
                subtitle = "2"
            )
            HorizontalDivider()
            ProfileItem(
                title = "Rol",
                subtitle = "Yönetici"
            )
            ProfileItem(
                title = "Gruplar",
                subtitle = "Support"
            )
            ProfileItem(
                title = "Takma ad",
                subtitle = "-"
            )
            ProfileItem(
                title = "İmza",
                subtitle = "-"
            )
            HorizontalDivider()
            ProfileItem(
                title = "Birincil e-posta",
                subtitle = state.user.email
            )
            HorizontalDivider()
            ProfileItem(
                title = "Etiketler",
                subtitle = "-"
            )
            ProfileItem(
                title = "Org",
                subtitle = "Test"
            )
            ProfileItem(
                title = "Dil",
                subtitle = "Türkçe"
            )
        }
    }
}

@Preview
@Composable
private fun PSP() {
    GrispiSupportTheme {
        ProfileScreenContent(
            state = ProfileState(
                user = currentUser
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            navigateUp = {}
        )
    }
}