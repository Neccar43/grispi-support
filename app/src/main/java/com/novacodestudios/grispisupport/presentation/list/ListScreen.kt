package com.novacodestudios.grispisupport.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.novacodestudios.grispisupport.presentation.list.component.ListTopBar
import com.novacodestudios.grispisupport.presentation.list.component.SortSheet
import com.novacodestudios.grispisupport.presentation.list.component.StdSearchBarWithAnimation
import com.novacodestudios.grispisupport.presentation.list.component.TicketItem
import com.novacodestudios.grispisupport.presentation.list.component.TicketList
import com.novacodestudios.grispisupport.presentation.list.component.TicketSortRow
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navigateDetail: (String) -> Unit,
    onMenuClick: () -> Unit,
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is ListViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    ListScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateDetail = navigateDetail,
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenContent(
    state: ListState,
    snackbarHostState: SnackbarHostState,
    onEvent: (ListEvent) -> Unit,
    navigateDetail: (String) -> Unit,
    onMenuClick: () -> Unit,
) {
    var isActive by remember { mutableStateOf(false) }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListTopBar(
                onSearchClick = { isActive = true },
                onMenuClick = onMenuClick
            )
        },
    ) { paddingValues ->
        var isSortSheetVisible by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TicketSortRow(
                sortOptions = state.sortOptions,
                onClick = { isSortSheetVisible = true }
            )
            TicketList(state.tickets, onTicketClick = { navigateDetail(it.id) })
        }
        if (isSortSheetVisible) {
            SortSheet(
                onDismiss = { isSortSheetVisible = false },
                currentSortOptions = state.sortOptions,
                isAscending = state.isAscending,
                onAscendingChange = {},
                onSortOptionChange = {}
            )
        }

    }
    StdSearchBarWithAnimation(
        active = isActive,
        onActiveChange = { isActive = it },
        query = state.query ?: "",
        onQueryChange = { onEvent(ListEvent.OnQueryChanged(it)) },
        onSearch = { onEvent(ListEvent.OnSearchClicked) },
        placeholderText = stringResource(R.string.search_placeholder),
    ) {
        state.searchTickets.forEach { ticket ->
            TicketItem(
                ticket = ticket,
                onClick = { navigateDetail(ticket.id) }
            )
        }
    }
}

enum class SortOptions {
    DEFAULT,
    REQUESTED,
    ASSIGNED,
    CREATED_DATE,
    UPDATED_DATE,
    PRIORITY,
    STATUS
}
@Composable
fun SortOptions.toUiText(): String {
    return when (this) {
        SortOptions.DEFAULT -> stringResource(R.string.sort_default)
        SortOptions.REQUESTED -> stringResource(R.string.sort_requested)
        SortOptions.ASSIGNED -> stringResource(R.string.sort_assigned)
        SortOptions.CREATED_DATE -> stringResource(R.string.sort_created_date)
        SortOptions.UPDATED_DATE -> stringResource(R.string.sort_updated_date)
        SortOptions.PRIORITY -> stringResource(R.string.sort_priority)
        SortOptions.STATUS -> stringResource(R.string.sort_status)
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    GrispiSupportTheme {
        ListScreenContent(
            state = ListState(tickets = dummyTicketList),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            navigateDetail = {},
            onMenuClick = {}
        )
    }
}


