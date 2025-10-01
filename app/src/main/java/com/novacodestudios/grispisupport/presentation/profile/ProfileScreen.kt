package com.novacodestudios.grispisupport.presentation.profile

import androidx.annotation.Keep
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.component.GrspTextField
import com.novacodestudios.grispisupport.presentation.component.SipAlertDialog
import com.novacodestudios.grispisupport.presentation.model.UserRole
import com.novacodestudios.grispisupport.presentation.profile.component.ProfileItem
import com.novacodestudios.grispisupport.presentation.profile.component.ProfileTopBar
import com.novacodestudios.grispisupport.presentation.settings.component.SettingsItem
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateFilteredTickets: (String, UserTicketFilter) -> Unit,
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
        navigateFilteredTickets = navigateFilteredTickets
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ProfileEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateFilteredTickets: (String, UserTicketFilter) -> Unit
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
        var isNameDialogVisible by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalDivider()
            ProfileItem(
                title = stringResource(id = R.string.full_name),
                subtitle = state.user.name,
                onClick = { isNameDialogVisible = true }
            )
            ProfileItem(
                title = stringResource(id = R.string.email),
                subtitle = state.user.email,
            )
            if (state.isCurrentUser) { // sadece kendi profiline girince gözüksün yada ayarlar ekrnaına taşı
                SettingsItem(
                    headlineText = stringResource(id = R.string.reset_password),
                    onClick = {}
                )
            }

            ProfileItem(
                title = stringResource(id = R.string.phone),
                subtitle = state.user.phone ?: "-",
            )
            ProfileItem(
                title = stringResource(id = R.string.language),
                subtitle = stringResource(id = R.string.turkish)
            )
            HorizontalDivider()
            ProfileItem(
                title = stringResource(id = R.string.role),
                subtitle = state.user.role.toUiString()
            )
            ProfileItem(
                title = stringResource(id = R.string.organization),
                subtitle = state.user.organization ?: "-"
            )
            ProfileItem(
                title = stringResource(id = R.string.groups),
                subtitle = state.user.groups.joinToString(", ").ifEmpty { "-" }
            )

            HorizontalDivider()
            ProfileItem(
                title = stringResource(id = R.string.assigned_tickets),
                subtitle = state.assignedTicketCount.toString(),
                onClick = { navigateFilteredTickets(state.user.id, UserTicketFilter.ASSIGNEE) }
            )
            ProfileItem(
                title = stringResource(id = R.string.requested_tickets),
                subtitle = state.requestedTicketCount.toString(),
                onClick = { navigateFilteredTickets(state.user.id, UserTicketFilter.REQUEST) }
            )
            ProfileItem(
                title = stringResource(id = R.string.followed_tickets),
                subtitle = state.followedTicketCount.toString(),
                onClick = { navigateFilteredTickets(state.user.id, UserTicketFilter.FOLLOW) }
            )
            ProfileItem(
                title = stringResource(id = R.string.mentioned_tickets),
                subtitle = state.mentionedTicketCount.toString(),
                onClick = { navigateFilteredTickets(state.user.id, UserTicketFilter.MENTION) }
            )
        }

        if (isNameDialogVisible) {
            SipAlertDialog(
                onDismiss = { isNameDialogVisible = false },
                title = stringResource(id = R.string.full_name),
                text = {
                    GrspTextField(
                        value = state.name,
                        onValueChange = { onEvent(ProfileEvent.OnNameChange(it)) },
                        placeholder = stringResource(id = R.string.full_name)
                    )
                },
                confirmButtonText = stringResource(id = R.string.ok),
                onConfirm = {
                    onEvent(ProfileEvent.OnConfirmName)
                    isNameDialogVisible = false
                },
                dismissButtonText = stringResource(id = R.string.cancel),
            )
        }
    }
}
@Keep
enum class UserTicketFilter {
    ASSIGNEE,
    REQUEST,
    FOLLOW,
    MENTION
}

@Composable
fun UserTicketFilter.toUiString(): String {
    return when (this) {
        UserTicketFilter.ASSIGNEE -> stringResource(id = R.string.assigned_tickets)
        UserTicketFilter.REQUEST -> stringResource(id = R.string.requested_tickets)
        UserTicketFilter.FOLLOW -> stringResource(id = R.string.followed_tickets)
        UserTicketFilter.MENTION -> stringResource(id = R.string.mentioned_tickets)
    }
}

@Preview
@Composable
private fun PSP() {
    GrispiSupportTheme {
        ProfileScreenContent(
            state = ProfileState(
                //user = currentUser
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            navigateUp = {},
            navigateFilteredTickets = { _, _ -> }
        )
    }
}


@Composable
fun UserRole.toUiString(): String {
    return when (this) {
        UserRole.END_USER -> stringResource(id = R.string.role_end_user)
        UserRole.AGENT -> stringResource(id = R.string.role_agent)
    }
}
