package com.novacodestudios.grispisupport.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.detail.component.ConversationSection
import com.novacodestudios.grispisupport.presentation.detail.component.DetailSection
import com.novacodestudios.grispisupport.presentation.detail.component.DetailTopBar
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import com.novacodestudios.grispisupport.presentation.util.messagesT1
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is DetailViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    DetailScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    state: DetailState,
    snackbarHostState: SnackbarHostState,
    onEvent: (DetailEvent) -> Unit,
    navigateUp: () -> Unit
) {
    if (state.ticket == null) {
        // TODO: Handle et
        return
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailTopBar(
                state.ticket,
                navigateUp = navigateUp,
                isConversation = state.isConversation
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = if (state.isConversation) 0 else 1
            ) {
                Tab(
                    selected = state.isConversation,
                    onClick = { onEvent(DetailEvent.OnActiveTabChange(true)) },
                    text = { Text("Sohbet") }
                )
                Tab(
                    selected = !state.isConversation,
                    onClick = { onEvent(DetailEvent.OnActiveTabChange(false)) },
                    text = { Text("Detay") }
                )
            }

            if (state.isConversation) {
                ConversationSection(
                    ticket = state.ticket,
                    messageList = state.messageList,
                    replyValue = state.replyText,
                    onReplyChange = { onEvent(DetailEvent.OnReplyTextChange(it)) },
                )
            } else {
                DetailSection(
                    ticket = state.ticket,
                    replyValue = state.replyText,
                    onReplyChange = { onEvent(DetailEvent.OnReplyTextChange(it)) }
                )
            }

        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    GrispiSupportTheme {
        DetailScreenContent(
            state = DetailState(
                ticket = dummyTicketList.first(),
                isConversation = true,
                messageList = messagesT1.sortedBy { it.sentAt }),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            navigateUp = {}
        )
    }
}