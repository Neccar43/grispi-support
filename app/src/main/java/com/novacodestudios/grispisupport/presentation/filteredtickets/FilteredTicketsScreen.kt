package com.novacodestudios.grispisupport.presentation.filteredtickets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.list.component.TicketList
import com.novacodestudios.grispisupport.presentation.profile.toUiString
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FilteredTicketsScreen(
    viewModel: FilteredTicketsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateDetail: (String) -> Unit,
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is FilteredTicketsViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(
                    state.message
                )
            }
        }
    }
    FilteredTicketsScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp,
        navigateDetail = navigateDetail,
    )
}

@Composable
fun FilteredTicketsScreenContent(
    state: FilteredTicketsState,
    snackbarHostState: SnackbarHostState,
    onEvent: (FilteredTicketsEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateDetail: (String) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { FilterTicketsTopBar(navigateUp = navigateUp, title = state.filter.toUiString()) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TicketList(state.tickets, onTicketClick = { navigateDetail(it.id) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTicketsTopBar(navigateUp: () -> Unit, title: String) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    null
                )
            }
        },
    )
}

