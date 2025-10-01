package com.novacodestudios.grispisupport.presentation.settings

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.detail.component.DetailDialog
import com.novacodestudios.grispisupport.presentation.detail.component.DialogRadioGroup
import com.novacodestudios.grispisupport.presentation.settings.component.SettingsItem
import com.novacodestudios.grispisupport.presentation.settings.component.SettingsTopBar
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

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
    val context = LocalContext.current
    var isThemeDialogVisible by remember { mutableStateOf(false) }
    var isLanguageDialogVisible by remember { mutableStateOf(false) }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { SettingsTopBar(navigateUp = navigateUp) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HorizontalDivider()

            SettingsItem(
                onClick = { navigateProfile() },
                headlineText = stringResource(id = R.string.profile),
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { }, // TODO: bildirim ayarları ekranına git veya dialog aç
                headlineText = stringResource(id = R.string.notifications),
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { isThemeDialogVisible = true },
                headlineText = stringResource(id = R.string.theme),
                supportingText = state.theme.toUiText()
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { isLanguageDialogVisible = true },
                headlineText = stringResource(id = R.string.language),
                supportingText = state.language.toUiText()
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { },
                headlineText = stringResource(id = R.string.contact_support),
            )
            HorizontalDivider()
            SettingsItem(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://support.grispi.com".toUri())
                    context.startActivity(intent)
                },
                headlineText = stringResource(id = R.string.help_center),
            )
            HorizontalDivider()
            SettingsItem(
                onClick = { navigateSignIn() },
                headlineText = stringResource(id = R.string.sign_out),
            )
            HorizontalDivider()

            SettingsItem(
                onClick = { },
                headlineText = stringResource(id = R.string.version),
                supportingText = stringResource(id = R.string.version_number)
            )
        }
    }

    if (isThemeDialogVisible) {
        var selectedOption by remember { mutableStateOf(state.theme) }
        DetailDialog(
            onDismiss = { isThemeDialogVisible = false },
            title = stringResource(id = R.string.theme),
            text = {
                DialogRadioGroup(
                    options = ThemeOption.entries.map { it.toUiText() },
                    selectedOption = selectedOption.toUiText(),
                    onOptionSelected = { selectedOption = displayNameToTheme(context, it) },
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
            title = stringResource(id = R.string.language),
            text = {
                DialogRadioGroup(
                    options = LanguageOption.entries.map { it.toUiText() },
                    selectedOption = selectedOption.toUiText(),
                    onOptionSelected = { selectedOption = displayNameToLanguage(context, it) },
                )
            },
            onConfirm = {
                setAppLanguageForNewApi(context = context, languageOption = selectedOption)
                (context as? Activity)?.recreate()
                onEvent(SettingsEvent.OnLanguageSelected(selectedOption))
                isLanguageDialogVisible = false
            },
        )
    }
}

enum class ThemeOption() {
    LIGHT,
    DARK,
    SYSTEM_DEFAULT
}

@Composable
fun ThemeOption.toUiText(): String {
    return when (this) {
        ThemeOption.LIGHT -> stringResource(id = R.string.light)
        ThemeOption.DARK -> stringResource(id = R.string.dark)
        ThemeOption.SYSTEM_DEFAULT -> stringResource(id = R.string.system_default)
    }
}

fun displayNameToTheme(context: Context, displayName: String): ThemeOption {
    return when (displayName) {
        context.getString(R.string.light) -> ThemeOption.LIGHT
        context.getString(R.string.dark) -> ThemeOption.DARK
        context.getString(R.string.system_default) -> ThemeOption.SYSTEM_DEFAULT
        else -> ThemeOption.SYSTEM_DEFAULT
    }
}


enum class LanguageOption {
    TURKISH,
    ENGLISH
}

fun displayNameToLanguage(context: Context, displayName: String): LanguageOption {
    return when (displayName) {
        context.getString(R.string.turkish) -> LanguageOption.TURKISH
        context.getString(R.string.english) -> LanguageOption.ENGLISH
        else -> LanguageOption.TURKISH
    }
}

@Composable
fun LanguageOption.toUiText(): String {
    return when (this) {
        LanguageOption.TURKISH -> stringResource(id = R.string.turkish)
        LanguageOption.ENGLISH -> stringResource(id = R.string.english)
    }
}


fun LanguageOption.toLanguageCode(): String {
    return when (this) {
        LanguageOption.TURKISH -> "tr"
        LanguageOption.ENGLISH -> "en"
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

fun setAppLanguageForNewApi(context: Context, languageOption: LanguageOption) {
    val languageCode = languageOption.toLanguageCode()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(languageCode)
    } else {
        setAppLanguageForLegacy(context, languageCode)
        Log.d(TAG, "changeLanguage: Language changed to $languageCode")
    }
}

fun setAppLanguageForLegacy(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Log.d(TAG, "changeLanguage: Changing language to $languageCode")
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return
    }
}
