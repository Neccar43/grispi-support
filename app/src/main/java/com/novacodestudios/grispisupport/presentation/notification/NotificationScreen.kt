package com.novacodestudios.grispisupport.presentation.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.notification.component.NotificationList
import com.novacodestudios.grispisupport.presentation.notification.component.NotificationTopBar
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateDetail: (String) -> Unit
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is NotificationViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(
                    state.message
                )
            }
        }
    }
    NotificationScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp,
        navigateDetail = navigateDetail
    )
}

@Composable
fun NotificationScreenContent(
    state: NotificationState,
    snackbarHostState: SnackbarHostState,
    onEvent: (NotificationEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateDetail: (String) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            NotificationTopBar(
                notificationSize = state.unreadCount,
                navigateUp = navigateUp,
                markAllAsRead = { onEvent(NotificationEvent.MarkAllAsRead) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            NotificationList(
                notifications = state.notifications,
                onNotificationClick = { navigateDetail(it.ticket.id) }
            )
        }
    }
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    GrispiSupportTheme {
        NotificationScreenContent(
            state = NotificationState(
                //notifications = dummyNotifications
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            navigateUp = {},
            navigateDetail = {}
        )
    }
}